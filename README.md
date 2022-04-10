
# 领域模型太复杂怎么破？

## 背景

按照 DDD 的设计思想，调用方仅能操作 Aggregate Root，而不能单独针对某个非 Aggregate Root 的 Entity 直接操作。但有时候业务模型就是很复杂，如果拆分成多个 Aggregate，相互之间的依赖会让业务逻辑的实现变得更复杂。

## 主流方案

有什么办法削弱复杂的 Aggregate 产生的负面影响呢？常见的主流方案是 **Change-Tracking** 变化追踪，如果能知道哪些值变化了，就只需要更新有变化的值。就不会出现只更新了一个状态值，整个 Aggregate 相关的数据模型都更新了一遍的尴尬场景了。常见的实现方式有

##### 1、基于Snapshot的方案：

当数据从DB里取出来后，在内存中保存一份snapshot，然后在数据写入时和snapshot比较。常见的实现如Hibernate。

##### 2、基于Proxy的方案：

当数据从DB里取出来后，通过weaving的方式将所有setter都增加一个切面来判断setter是否被调用以及值是否变更，如果变更则标记为Dirty。在保存时根据Dirty判断是否需要更新。常见的实现如Entity Framework。



## 新的实践

现实中的 Aggregate 虽然复杂，但子实体的更新并不频繁，大概率是聚合的状态变更，典型的例子就是电商的交易。大部分场景只需要用到交易主表的信息，或只需要更新交易的状态，但是构造 Aggregate 时会把所有的非 Aggregate Root 的 Entity 也构造出来。所以主流方案用到现在的交易系统不仅实现成本较高，还不能解决所有的痛点。但受到上面方案的启发，想到了一种更轻量的方案。

#### LazyRead&Tracking

主要的思路就是在构造 Aggregate 时，并不直接构造所有的 Entity ，而只构造主 Entity 并构造其他 Entity 的改造方法（🚫禁止套娃）。然后在持久化时只处理正在构造过的实体。那么问题来了。

- 如何实现在用的时候才构造呢？
- 重复读取的时候怎么保证只构造一次呢？
- 持久化的时候怎么才能感知到哪些实体是构造过的呢？
- 新创建的实体怎么在持久化时正确处理呢？

答案就是 **AwareSupplier**（现代码已简化为 **As**）。

```Java
public interface AwareSupplier<T> extends Supplier<T> {
    boolean isHere();
}
```

`AwareSupplier` 继承自 `Supplier`。相比 `Supplier` 多定义了一个方法 `isHere()` 用于表示调用 `get()` 可以获取的 `T` 的实例是否已经构造好了。

光看 `AwareSupplier` 这个接口还得不到完整的答案，我们继续看一下这个接口的实现。

我定义了两个实现类 `SelfSupplier` 和 `AwareMemoizeSupplier`，都定义在 `Suppliers` 内部。

```Java
private static class SelfSupplier<T> implements AwareSupplier<T> {
    private final T goods;

    SelfSupplier(@NotNull T goods) {
        this.goods = goods;
    }

    @Override
    public boolean isGoodsHere() {
        return true;
    }

    @Override
    public T get() {
        return goods;
    }
}
```

可以看到 `SelfSupplier` 的 `isHere()` 方法永远返回 `true`，而 `get()` 方法返回实例是在构造 `SelfSupplier`时直接传入的。很明显，这样的实现是用在构造新创建的实体时。

----

```java
private static class AwareMemoizeSupplier<T> implements AwareSupplier<T> {
    private final AtomicReference<T> value = new AtomicReference<>();
    private final Supplier<T> supplier;
    private boolean callInnerGet = false;

    AwareMemoizeSupplier(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean isHere() {
        return callInnerGet;
    }

    @Override
    public T get() {
        T val = value.get();
        if (val == null) {
            synchronized (value) {
                val = value.get();
                if (val == null) {
                    val = supplier.get();
                    callInnerGet = true;
                    value.set(val);
                }
            }
        }
        return val;
    }
}
```

与`SelfSupplier` 不同的是， `AwareMemoizeSupplier` 的构造函数的入参是 `Supplier` 并且多了两个私有变量，一个用来标记是否调用过`Supplier` 的 `get()` 方法，一个用来缓存 `get()` 方法返回的结果。这样就可以解决上面的疑问了。在调用 `AwareMemoizeSupplier` 的 `get()` 方法时，会先尝试从缓存中获取，获取不到才真正调用 `Supplier` 的 `get()` 方法去获取，并且会就标记变量设置为 `true`。
这里还加了 `synchronized` 主要考虑到通用性，如果确定不会有线程安全问题可以去掉。

#### 命名的来由

`AwareSupplier` 是想表达具有感知能力的 `Supplier`。

`AwareMemoizeSupplier`是想表达具有感知能力且拥有记忆的 `Supplier`。

`SelfSupplier`是想表达一种特殊的、可以提供自我获取能力的 `Supplier`。

如果你想到更好的命名，请及时告诉我，或者直接提MR。