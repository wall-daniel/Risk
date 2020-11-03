package risk;

import java.awt.*;

public class InputFrame {

    Component[] components;


    public InputFrame(Component... components){
        this.components = components;
    }

    public InputFrame(Class c){
        c.getDeclaredFields();


    }




    public String getInputFromComponent(String componentName){
        String text = "";
        for (Component component : components)
            if (component.getName().equals(componentName))
                text = ((TextField) component).getText();
        return text;
    }


}
