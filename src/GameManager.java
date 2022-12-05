import com.fasterxml.jackson.databind.node.ArrayNode;
import utils.Input;

public class GameManager {

    private final Input inputData;

    private final ArrayNode outputData;

    public GameManager(final Input inputData, final ArrayNode outputData) {
        this.inputData = inputData;
        this.outputData = outputData;
    }

    /**
     *
     * @return      the generated .json output for the current test
     */
    public ArrayNode generateOutput() {
        return outputData;
    }
}
