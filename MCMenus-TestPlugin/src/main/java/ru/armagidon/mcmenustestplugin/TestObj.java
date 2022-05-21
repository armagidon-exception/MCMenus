package ru.armagidon.mcmenustestplugin;

import ru.armagidon.mcmenusapi.parser.tags.DataModifier;

public interface TestObj
{
    void run();
    @DataModifier
    void setTestCheckButton(boolean testCheckButton);
}
