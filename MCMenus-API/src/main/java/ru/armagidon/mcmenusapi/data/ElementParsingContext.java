package ru.armagidon.mcmenusapi.data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.armagidon.mcmenusapi.menu.Menu;
import ru.armagidon.mcmenusapi.menu.MenuPanel;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ElementParsingContext<A extends Annotation, I>
{

    Supplier<I> getDataGetter();
    Consumer<I> getDataSetter();
    Consumer<Object[]> getMethodInvoker();
    A getAnnotationData();
    Menu getOwner();
    MenuPanel getParent();

    static <A extends Annotation, I> ElementParsingContextImpl.ElementParsingContextImplBuilder<A, I> builder() {
        return ElementParsingContextImpl.builder();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    final class ElementParsingContextImpl<A extends Annotation, I> implements ElementParsingContext<A, I>{
        A annotationData;
        @Builder.Default Consumer<Object[]> methodInvoker = (o) -> {};
        @Builder.Default Consumer<I> dataSetter = (o) -> {};
        @Builder.Default Supplier<I> dataGetter = () -> null ;
        Menu owner;
        MenuPanel parent;

        ElementParsingContextImplBuilder<A, I> modifer() {
            var builder = new ElementParsingContextImplBuilder<A, I>();
            builder.dataSetter(this.dataSetter);
            builder.dataGetter(this.dataGetter);
            builder.methodInvoker(methodInvoker);
            builder.annotationData(annotationData);
            builder.owner(owner);
            builder.parent(parent);
            return builder;
        }
    }

}
