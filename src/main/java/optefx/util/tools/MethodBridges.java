
package optefx.util.tools;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Enrique Urra C.
 */
public final class MethodBridges
{
    @FunctionalInterface
    public interface P2<T1, T2> 
    {
        void call(T1 a1, T2 a2);
    }
    
    @FunctionalInterface
    public interface P3<T1, T2, T3> 
    {
        void call(T1 a1, T2 a2, T3 a3);
    }
    
    @FunctionalInterface
    public interface P4<T1, T2, T3, T4>
    {
        void call(T1 a1, T2 a2, T3 a3, T4 a4);
    }
    
    @FunctionalInterface
    public interface P5<T1, T2, T3, T4, T5>
    {
        void call(T1 a1, T2 a2, T3 a3, T4 a4, T5 a5);
    }
    
    @FunctionalInterface
    public interface P6<T1, T2, T3, T4, T5, T6>
    {
        void call(T1 a1, T2 a2, T3 a3, T4 a4, T5 a5, T6 a6);
    }
    
    @FunctionalInterface
    public interface F2<T, T1, T2> 
    {
        T call(T1 a1, T2 a2);
    }
    
    @FunctionalInterface
    public interface F3<T, T1, T2, T3> 
    {
        T call(T1 a1, T2 a2, T3 a3);
    }
    
    @FunctionalInterface
    public interface F4<T, T1, T2, T3, T4>
    {
        T call(T1 a1, T2 a2, T3 a3, T4 a4);
    }
    
    @FunctionalInterface
    public interface F5<T, T1, T2, T3, T4, T5>
    {
        T call(T1 a1, T2 a2, T3 a3, T4 a4, T5 a5);
    }
    
    @FunctionalInterface
    public interface F6<T, T1, T2, T3, T4, T5, T6>
    {
        T call(T1 a1, T2 a2, T3 a3, T4 a4, T5 a5, T6 a6);
    }
    
    public static <T1> Runnable proc(Consumer<T1> op, T1 arg)
    {
        return () -> op.accept(arg);
    }
    
    public static <T1, T2> Runnable proc(
        BiConsumer<T1, T2> op, T1 a1, T2 a2)
    {
        return () -> op.accept(a1, a2);
    }
    
    public static <T1, T2, T3> Runnable proc(
        P3<T1, T2, T3> op, T1 a1, T2 a2, T3 a3)
    {
        return () -> op.call(a1, a2, a3);
    }
    
    public static <T1, T2, T3, T4> Runnable proc(
        P4<T1, T2, T3, T4> op, T1 a1, T2 a2, T3 a3, T4 a4)
    {
        return () -> op.call(a1, a2, a3, a4);
    }
    
    public static <T1, T2, T3, T4, T5> Runnable proc(
        P5<T1, T2, T3, T4, T5> op, T1 a1, T2 a2, T3 a3, T4 a4, T5 a5)
    {
        return () -> op.call(a1, a2, a3, a4, a5);
    }
    
    public static <T1, T2, T3, T4, T5, T6> Runnable proc(
        P6<T1, T2, T3, T4, T5, T6> op, T1 a1, T2 a2,  T3 a3,T4 a4, T5 a5, T6 a6)
    {
        return () -> op.call(a1, a2, a3, a4, a5, a6);
    }
    
    public static <T, T1> Supplier<T> func(Function<T1, T> cond, T1 arg)
    {
        return () -> cond.apply(arg);
    }
    
    public static <T, T1, T2> Supplier<T> func(
        F2<T, T1, T2> cond, T1 a1, T2 a2)
    {
        return () -> cond.call(a1, a2);
    }
    
    public static <T, T1, T2, T3> Supplier<T> func(
        F3<T, T1, T2, T3> cond, T1 a1, T2 a2, T3 a3)
    {
        return () -> cond.call(a1, a2, a3);
    }
    
    public static <T, T1, T2, T3, T4> Supplier<T> func(
        F4<T, T1, T2, T3, T4> cond, T1 a1, T2 a2, T3 a3, T4 a4)
    {
        return () -> cond.call(a1, a2, a3, a4);
    }
    
    public static <T, T1, T2, T3, T4, T5> Supplier<T> func(
        F5<T, T1, T2, T3, T4, T5> cond, T1 a1, T2 a2, T3 a3, T4 a4, T5 a5)
    {
        return () -> cond.call(a1, a2, a3, a4, a5);
    }
    
    public static <T, T1, T2, T3, T4, T5, T6> Supplier<T> func(
        F6<T, T1, T2, T3, T4, T5, T6> cond, T1 a1, T2 a2, T3 a3, T4 a4, T5 a5, T6 a6)
    {
        return () -> cond.call(a1, a2, a3, a4, a5, a6);
    }

    private MethodBridges()
    {
    }
}
