package Agents;

import Evaluator.ClassicTerminalStateEvaluator;
import Evaluator.MultiNeuralNetworkLeafEvaluator;
import MCTSStrategies.Backpropagation.FixedEarlyTerminationBackprop;
import MCTSStrategies.FinalMoveSelection.RobustChild;
import MCTSStrategies.Rescaler.Softmax;
import MCTSStrategies.Selection.ImplicitUCTRescaledExploration4;
import MCTSStrategies.Selection.ImplicitUCTRescaledExploration5;
import Training.LearningManager;
import game.Game;
import search.mcts.playout.RandomPlayout;

/**
 * MCTS search algorithm using neural networks with Implicit UCT, no play-outs, while
 * backpropagating the estimated value by the network, and robust child. Instead of using a fixed exploration constant,
 * the exploration gets multiplied with a probability (similar to Polygames and AlphaZero). The exploration probability
 * is calculated using softmax with temperature (T=1/parent visits). The ln is removed from the exploration part
 * (Inspired by Polygames).
 */
public class ImplicitMCTSNNNPRescaledExploration5 extends MCTS {

    //-------------------------------------------------------------------------

    /** Path to the neural network */
    String pathName;

    //-------------------------------------------------------------------------

    /**
     * Constructor with the path to the desired neural network as string
     * (influence estimated value = 0.8, QInit=PARENT, 4 threads)
     * @param pathName Path to the desired neural network
     */
    public ImplicitMCTSNNNPRescaledExploration5(String pathName) {
        super(new ImplicitUCTRescaledExploration5(.8, new Softmax()), new RandomPlayout(0),
                new FixedEarlyTerminationBackprop(), new RobustChild());

        this.pathName = pathName;

        this.setNumThreads(4);
        this.setQInit(QInit.PARENT);
    }

    /**
     * Perform desired initialisation before starting to play a game
     * Initialise the parent and both GameStateEvaluators
     *
     * @param game The game that we'll be playing
     * @param playerID The player ID for the AI in this game
     */
    public void initAI(Game game, int playerID) {
        super.initParent(game, playerID);

        this.setLeafEvaluator(new MultiNeuralNetworkLeafEvaluator(game,
                LearningManager.loadNetwork(pathName, false), this.numThreads), game);
        this.setTerminalStateEvaluator(new ClassicTerminalStateEvaluator());
    }
}