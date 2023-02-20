package Agents;

import Evaluator.ClassicTerminalStateEvaluator;
import Evaluator.ParallelNeuralNetworkLeafEvaluator;
import MCTSStrategies.FinalMoveSelection.RobustChild;
import MCTSStrategies.Selection.ImplicitUCT;
import Training.LearningManager;
import game.Game;
import search.mcts.backpropagation.MonteCarloBackprop;
import search.mcts.playout.RandomPlayout;

/**
 * MCTS search algorithm using neural networks with implicit UCT, no play-out, monte carlo
 * backpropagation, and robust child with solver.
 */
public class ImplicitMCTSNNRP3 extends MCTS {
    //-------------------------------------------------------------------------

    /** Path to the neural network */
    String pathName;

    //-------------------------------------------------------------------------

    /**
     * Constructor with the path to the desired neural network as string
     * (solver=true, influence estimated value=1, QInit=DRAW, 4 threads)
     * @param pathName Path to the desired neural network
     */
    public ImplicitMCTSNNRP3(String pathName) {
        super(new ImplicitUCT(1, Math.sqrt(2)), new RandomPlayout(0),
                new MonteCarloBackprop(), new RobustChild());

        this.pathName = pathName;

        this.setNumThreads(5);
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
                LearningManager.loadNetwork(pathName, false), 64), game);
        this.setTerminalStateEvaluator(new ClassicTerminalStateEvaluator());
    }
}