package Agents;

import Evaluator.ClassicTerminalStateEvaluator;
import Evaluator.ParallelNeuralNetworkLeafEvaluator;
import MCTSStrategies.Backpropagation.FixedEarlyTerminationBackprop;
import MCTSStrategies.FinalMoveSelection.RobustChild;
import MCTSStrategies.Rescaler.MultiplyDifferences;
import MCTSStrategies.Selection.ImplicitUCTRescaled;
import Training.LearningManager;
import game.Game;
import search.mcts.playout.RandomPlayout;

/**
 * MCTS search algorithm using neural networks (parallel inference) with Implicit UCT, no play-outs, while
 * backpropagating the estimated value by the network, and robust child with solver. Instead of using the estimated
 * values directly, the estimated values get rescaled by multiplying the difference to the mean.
 */
public class ImplicitMCTSNNNPRescaled4 extends MCTS {

    //-------------------------------------------------------------------------

    /** Path to the neural network */
    String pathName;

    //-------------------------------------------------------------------------

    /**
     * Constructor with the path to the desired neural network as string
     * (solver=true, influence estimated value = 0.8, exploration=0.001, multiplier=2, QInit=DRAW, 12 threads)
     * @param pathName Path to the desired neural network
     */
    public ImplicitMCTSNNNPRescaled4(String pathName) {
        super(new ImplicitUCTRescaled(.8, .001f, new MultiplyDifferences(2)),
                new RandomPlayout(0),new FixedEarlyTerminationBackprop(), new RobustChild());

        this.pathName = pathName;

        this.setNumThreads(12);
        this.setUseSolver(true);
        this.setQInit(QInit.DRAW);
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

        this.setLeafEvaluator(new ParallelNeuralNetworkLeafEvaluator(game,
                LearningManager.loadNetwork(pathName, false), 128), game);
        this.setTerminalStateEvaluator(new ClassicTerminalStateEvaluator());
    }
}