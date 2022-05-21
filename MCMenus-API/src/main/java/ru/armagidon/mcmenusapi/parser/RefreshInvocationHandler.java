package ru.armagidon.mcmenusapi.parser;

import lombok.AllArgsConstructor;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.parser.tags.DataModifier;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@AllArgsConstructor
public class RefreshInvocationHandler implements InvocationHandler
{

    Object original;
    MenuPanel panelToRefresh;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object returnObject = method.invoke(original, args);
        if (method.isAnnotationPresent(DataModifier.class)) {
            panelToRefresh.refresh(false);
        }
        return returnObject;
    }
}
