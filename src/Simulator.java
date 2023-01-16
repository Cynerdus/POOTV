import com.fasterxml.jackson.databind.node.ArrayNode;
import utils.Database;
import utils.structures.Action;
import utils.structures.Input;
import utils.facade.PageKeeper;

public class Simulator {
    private final Input inputData;
    private final ArrayNode outputData;

    private Database database;

    public Simulator(final Input inputData, final ArrayNode outputData) {
        this.inputData = inputData;
        this.outputData = outputData;

        database = new Database();
        database.addUsers(inputData.getUsers());
        database.addMovies(inputData.getMovies());

        simulateCommands();
    }

    /**
     *
     * @return      the generated .json output for the current test
     */
    public ArrayNode generateOutput() {
        return outputData;
    }

    /**
     *          run every registered action
     */
    public void simulateCommands() {
        PageKeeper pageKeeper = new PageKeeper(database, outputData);

        for (Action action : inputData.getActions()) {
            pageKeeper.processPageAction(action);

            if (inputData.getActions().indexOf(action) == inputData.getActions().size() - 1) {
                pageKeeper.processRecommendationsForPremium();
            }
        }
    }
}
