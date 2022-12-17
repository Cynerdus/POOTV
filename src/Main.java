import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import utils.structures.Input;

import java.io.File;
import java.io.IOException;

public final class Main {

    /**
     *
     * @param args          args[0] -> test path | args[1] -> results.out
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        //Input inputData = objectMapper.readValue(new File("checker/resources/in/basic_8.json"), Input.class);
        Input inputData = objectMapper.readValue(new File(args[0]), Input.class);
        ArrayNode outputData = objectMapper.createArrayNode();

        Simulator simulator = new Simulator(inputData, outputData);
        outputData = simulator.generateOutput();

        //objectWriter.writeValue(new File("out.json"), outputData);
        objectWriter.writeValue(new File(args[1]), outputData);
    }

    private Main() { }
}
