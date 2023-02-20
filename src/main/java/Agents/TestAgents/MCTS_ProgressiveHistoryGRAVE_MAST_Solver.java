package Agents.MCTSTest;

import Agents.MCTS;
import MCTSStrategies.FinalMoveSelection.RobustChild;
import MCTSStrategies.Selection.ProgressiveHistoryGRAVE;
import MCTSStrategies.Wrapper.debugFinalSelectionWrapper;
import search.mcts.backpropagation.MonteCarloBackprop;
import search.mcts.playout.MAST;

/**
 * MCTS implementation with a combination of Progressive History and GRAVE, while also using MAST, with solver. The
 * implementation uses Luddi's evaluation function.
 */
public class MCTS_ProgressiveHistoryGRAVE_MAST_Solver extends MCTS {
    /**
     * Constructor with no inputs
     */
    public MCTS_ProgressiveHistoryGRAVE_MAST_Solver() {
        super(new ProgressiveHistoryGRAVE(),
                new MAST(-1, .05f),
                new MonteCarloBackprop(),
                new RobustChild());

        this.friendlyName = "MCTS-Test";
        this.setNumThreads(6);
        this.setUseSolver(true);
    }
}