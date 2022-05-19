package ru.armagidon.mcmenusapi.parsers;


import ru.armagidon.mcmenusapi.menu.Menu;

public interface ElementParsingContext<I>
{
    I getInput();
    Object getDataModel();
    Menu getOwner();
    Object[] getAdditionalData();

    static <T> ElementParsingContext<T> createContext(T input, Object dataModel, Menu owner, Object... additionalData) {
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
            public Object[] getAdditionalData() {
                return additionalData;
            }
        };
    }

}
