package preevisiontosimulink;

import com.mathworks.engine.*;

public class Example1 {
    public static void main(String[] args) throws Exception {
        // Start the MATLAB engine
        MatlabEngine matlab = MatlabEngine.startMatlab();

        try {
            // Define the model name
            String modelName = "myModel";

            // Create a new Simulink model
            matlab.eval(modelName + " = new_system('" + modelName + "', 'Model')");

            // Add the Sine Wave block to the model
            matlab.eval("add_block('simulink/Sources/Sine Wave', '" + modelName + "/Sine')");

            // Add the Gain block to the model
            matlab.eval("add_block('simulink/Math Operations/Gain', '" + modelName + "/Gain')");

            // Add the Scope block to the model
            matlab.eval("add_block('simulink/Commonly Used Blocks/Scope', '" + modelName + "/Scope')");

            // Connect blocks
            matlab.eval("add_line('" + modelName + "', 'Sine/1', 'Gain/1')");
            matlab.eval("add_line('" + modelName + "', 'Gain/1', 'Scope/1')");

            // Set block parameters
            matlab.eval("set_param('" + modelName + "/Gain', 'Gain', '2')");

            // Save the model
            String modelFilePath = "" + modelName + ".slx";
            matlab.eval("save_system('" + modelName + "', '" + modelFilePath + "')");

            System.out.println("Sine Wave block added to the model: " + modelName);
        } finally {
            // Close the MATLAB engine
            matlab.close();
        }
    }
}
