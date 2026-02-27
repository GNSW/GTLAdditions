package com.gtladd.gtladditions.utils

import java.util.*
import java.util.function.*
import java.util.function.Function
import java.util.stream.*

class SingleStream<T : Any>(private val element: T) : Stream<T> {

    @Suppress("UNCHECKED_CAST")
    override fun filter(predicate: Predicate<in T>) = (if (predicate.test(element)) this else EMPTY) as Stream<T>

    override fun <R : Any> map(mapper: Function<in T, out R>) = createSingle<R>(mapper.apply(element))

    override fun mapToInt(mapper: ToIntFunction<in T>): IntStream = IntStream.of(mapper.applyAsInt(element))

    override fun mapToLong(mapper: ToLongFunction<in T>): LongStream = LongStream.of(mapper.applyAsLong(element))

    override fun mapToDouble(mapper: ToDoubleFunction<in T>): DoubleStream = DoubleStream.of(mapper.applyAsDouble(element))

    override fun <R> flatMap(mapper: Function<in T, out Stream<out R>>): Stream<R> = mapper.apply(element).map<R> { it }

    override fun flatMapToInt(mapper: Function<in T, out IntStream>): IntStream = mapper.apply(element)

    override fun flatMapToLong(mapper: Function<in T, out LongStream>): LongStream = mapper.apply(element)

    override fun flatMapToDouble(mapper: Function<in T, out DoubleStream>): DoubleStream = mapper.apply(element)

    override fun distinct(): Stream<T> = this

    override fun sorted(): Stream<T> = this

    override fun sorted(comparator: Comparator<in T>): Stream<T> = this

    override fun peek(action: Consumer<in T>): Stream<T> {
        action.accept(element)
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun limit(maxSize: Long) = if (maxSize <= 0) EMPTY as Stream<T> else this

    @Suppress("UNCHECKED_CAST")
    override fun skip(n: Long) = if (n > 0) EMPTY as Stream<T> else this

    override fun forEach(action: Consumer<in T>) = action.accept(element)

    override fun forEachOrdered(action: Consumer<in T>) = forEach(action)

    @Suppress("UNCHECKED_CAST")
    override fun toArray() = (element as? Array<Any>) ?: arrayOf<Any>(element)

    @Suppress("UNCHECKED_CAST")
    override fun <A> toArray(generator: IntFunction<Array<A>>): Array<A> = when (val e = element) {
        is Array<*> -> e as Array<A>
        else -> {
            val array = generator.apply(1)
            array[0] = e as A
            array
        }
    }

    override fun reduce(identity: T, accumulator: BinaryOperator<T>) = accumulator.apply(identity, element)

    override fun reduce(accumulator: BinaryOperator<T>) = Optional.of<T>(element)

    override fun <U> reduce(identity: U, accumulator: BiFunction<U, in T, U>, combiner: BinaryOperator<U>): U {
        return accumulator.apply(identity, element)
    }

    override fun <R> collect(supplier: Supplier<R>, accumulator: BiConsumer<R, in T>, combiner: BiConsumer<R, R>): R {
        val result = supplier.get()
        accumulator.accept(result, element)
        return result
    }

    override fun <R, A> collect(collector: Collector<in T, A, R>): R {
        val container = collector.supplier().get()
        collector.accumulator().accept(container, element)
        return collector.finisher().apply(container)
    }

    override fun min(comparator: Comparator<in T>) = Optional.of<T>(element)

    override fun max(comparator: Comparator<in T>): Optional<T> = min(comparator)

    override fun count() = 1L

    override fun anyMatch(predicate: Predicate<in T>) = predicate.test(element)

    override fun allMatch(predicate: Predicate<in T>) = anyMatch(predicate)

    override fun noneMatch(predicate: Predicate<in T>) = !predicate.test(element)

    override fun findFirst() = Optional.of<T>(element)

    override fun findAny(): Optional<T> = findFirst()

    override fun iterator() = mutableListOf(element).iterator()

    override fun spliterator() = mutableListOf(element).spliterator()

    override fun isParallel(): Boolean = false

    override fun sequential() = this

    override fun parallel() = this

    override fun unordered() = this

    override fun onClose(closeHandler: Runnable) = this

    override fun close() = Unit

    override fun toList() = listOf(element)

    companion object {
        val EMPTY: Stream<*> = Stream.empty<Any>()

        fun <T : Any> createSingle(element: T) = SingleStream(element)
    }
}
