package ru.armagidon.mcmenusapi.parser;


import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.menu.Menu;
import ru.armagidon.mcmenusapi.menu.MenuPanel;

public interface ElementParsingContext<I>
{
    I getInput();
    Object getDataModel();
    Menu getOwner();
    MenuPanel getParent();

    static <T> ElementParsingContext<T> createContext(T input, Object dataModel, Menu owner, MenuPanel parent) {
        return new ElementParsingContext<>() {
            @Override
            public T getInput() {
                return input;
            }

            @Override
            public Object getDataModel() {
                return dataModel;
            }

            @Override
            public Menu getOwner() {
                return owner;
            }

            @Override
            public MenuPanel getParent() {
                return parent;
            }
        };
    }

}
