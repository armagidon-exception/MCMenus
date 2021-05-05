import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import ru.armagidon.mcmenusapi.Menu;
import ru.armagidon.mcmenusapi.MenuPanel;
import ru.armagidon.mcmenusapi.menuelements.Icon;
import ru.armagidon.mcmenusapi.style.Title;

public class MenuTest
{
    @Test
    public void testMenu() {
        Menu menu = new Menu();
        MenuPanel panel = new MenuPanel();
        panel.getStyleSheet().setSize(3);
        panel.getStyleSheet().setTitle("LOL");

        panel.addElement(new Icon("test_icon", new ItemStack(Material.STONE)));
        panel.getStyleSheet().getStyle("test_icon").setTitle(Title.of("TEST STONE"));
        System.out.println(panel);
        menu.addPanel("test_panel", panel);
    }
}
