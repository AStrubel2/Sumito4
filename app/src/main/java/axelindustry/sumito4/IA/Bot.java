package axelindustry.sumito4.IA;

import android.app.Activity;
import android.media.MediaPlayer;

import java.util.LinkedList;

import axelindustry.sumito4.MainActivity;
import axelindustry.sumito4.R;

/**
 * Created by Clement on 09/04/2015.
 */
public class Bot extends Activity{
    private Board board;
    private MoveWayList moveWayList;
    private int difficulty;
    private int intensity=1;
    private int aggressivity;
    //private int seuil=-1;
    private MoveList[] possibles;
    private int iaColor;
    private int[][] aggressivityTable={{0, 0, 0, 0, 10, 10, 10, 10, 10},
            {0, 0, 0, 10, 14, 14, 14, 14, 10},
            {0, 0, 10, 14, 15, 15, 15, 14, 10},
            {0, 10, 14, 15, 16, 16, 15, 14, 10},
            {10, 14, 15, 16, 17, 16, 15, 14, 10},
            {10, 14, 15, 16, 16, 15, 14, 10, 0},
            {10, 14, 15, 15, 15, 14, 10, 0, 0},
            {10, 14, 14, 14, 14, 10, 0, 0, 0},
            {10, 10, 10, 10, 10, 0, 0, 0, 0}};

    public Bot(int iaColor,int difficulty,int aggressivity,Board board) {
        this.board=board;
        this.difficulty=difficulty;
        this.aggressivity=aggressivity;
        possibles=new MoveList[2];
        possibles[0]=null;
        possibles[1]=null;
        this.iaColor=iaColor;
        moveWayList=null;
    }

    private int sum(int color,Board board) {
        int s=0;
        for(int i=0;i<9;i++) {
            for(int j=0;j<9;j++) {
                if (board.isPlayer(i,j,color)) {
                    s+=aggressivityTable[i][j];
                }
                else if (board.isPlayer(i,j,1-color)){
                    s-=aggressivityTable[i][j]*aggressivity;
                }
            }
        }
        return s;
    }

    private int sum2(int color,Board board) {
        int s=sum(color,board);
        for(int i=0;i<9;i++) {
            for(int j=0;j<9;j++) {
                if (board.isPlayer(i,j,color)) {
                    if (board.isPlayer(i+1,j,color)) s+=intensity;
                    if (board.isPlayer(i+1,j+1,color)) s+=intensity;
                    if (board.isPlayer(i,j+1,color)) s+=intensity;
                    if (board.isPlayer(i,j-1,color)) s+=intensity;
                    if (board.isPlayer(i-1,j,color)) s+=intensity;
                    if (board.isPlayer(i-1,j-1,color)) s+=intensity;
                }
            }
        }
        return s;
    }

    private MoveWayList bestMove(int color,Board board) {
        int valMax=-10000;
        MoveWayList moveWayList=new MoveWayList();
        while (possibles[color]!=null) {
            MoveWayList moveWayListToCompare=possibles[color].getMoveWayList();
            possibles[color]=possibles[color].getNext();
            Board boardToTest=new Board(board);
            boardToTest.doMoveList(moveWayListToCompare);
            int valTemp=sum(color,boardToTest);
            if (valTemp>valMax) {
                valMax=valTemp;
                moveWayList=moveWayListToCompare;
            }
        }
        return moveWayList;
    }

    private MoveWayList bestMove2(int color,Board board) {
        int valMax=-10000;
        MoveWayList moveWayList=new MoveWayList();
        while (possibles[color]!=null) {
            MoveWayList moveWayListToCompare=possibles[color].getMoveWayList();
            possibles[color]=possibles[color].getNext();
            Board boardToTest=new Board(board);
            boardToTest.doMoveList(moveWayListToCompare);
            int valTemp=sum2(color,boardToTest);
            if (valTemp>valMax) {
                valMax=valTemp;
                moveWayList=moveWayListToCompare;
            }
        }
        return moveWayList;
    }

    private Move normalize(Move move) {
        Move moveNormalized=new Move(move);
        if(moveNormalized.n==1) {
            moveNormalized.u=0;
            moveNormalized.v=0;
        }
        int n=moveNormalized.n;
        int u=moveNormalized.u;
        int v=moveNormalized.v;
        if((n>1) && ( ((u!=0)|(v!=1)) && ((u!=-1)|(v!=1)) && ((u!=1)|(v!=0)) )) {
            moveNormalized.i+=(n-1)*u;
            moveNormalized.j+=(n-1)*v;
            moveNormalized.u=-u;
            moveNormalized.v=-v;
        }
        return(moveNormalized);
    }

    private void addMove(Move move,int color,MoveWayList moveWayList) {
        if (possibles[color]==null) {
            possibles[color]=new MoveList(move,moveWayList);
            return;
        }
        possibles[color].addMove(move,moveWayList);
    }

    private void addMoveWay(MoveWay moveWay) {
        if (moveWayList==null) {
            moveWayList=new MoveWayList(moveWay);
            return;
        }
        moveWayList.addMoveWay(moveWay);
    }

    public Boolean isPossible(int color,Move move) {
        if ((move.n<1)|(move.n>3)) return false;
        move=normalize(move);
        int i=move.i;
        int j=move.j;
        int n=move.n;
        int u=move.u;
        int v=move.v;
        int x=move.x;
        int y=move.y;
        if ((n>1) && ( ((u!=0)|(v!=1)) && ((u!=-1)|(v!=1)) && ((u!=1)|(v!=0)) )) {
            System.out.println("Error in normalization");
            return false;
        }
        for(int k=0;k<n;k++) {
            int a=i+k*u;
            int b=j+k*v;
            if (!((board.exist(a,b))&&(board.isPlayer(a,b,color)))) {
                return false;
            }
            // We now know that the bloc exists and belongs to color
        }
        if (n==1) {
            int a=i+x;
            int b=j+y;
            if ((board.exist(a,b)&&board.isPlayer(a,b,-1))) {
                MoveWay moveWay=new MoveWay(0,i,j,a,b);
                moveWayList=null;
                addMoveWay(moveWay);
                addMove(move,color,moveWayList);
                return true;
            }
            else return false;
        }
        int a;
        int b;
        int k;
        int l;
        if (((u==x)&&(v==y))||((u==-x)&&(v==-y))) {
            if ((u==x)&&(v==y)) {
                a=i+n*u;
                b=j+n*v;
                k=i;
                l=j;
            }
            else {
                a=i+x;
                b=j+y;
                k=i+(n-1)*u;
                l=j+(n-1)*v;
            }
            if (!board.exist(a, b)) return false;
            if (board.isPlayer(a,b,-1)) {
                moveWayList=null;
                MoveWay moveWay=new MoveWay(0,k,l,a,b);
                addMoveWay(moveWay);
                addMove(move,color,moveWayList);
                return true;
            }
            if (board.isPlayer(a, b, 1-color)) {
                if (!board.exist(a+x,b+y)) {
                    moveWayList=null;
                    MoveWay moveWay1=new MoveWay(1,k,l,a,b);
                    MoveWay moveWay2=new MoveWay(0,k,l,a,b);
                    addMoveWay(moveWay1);
                    addMoveWay(moveWay2);
                    addMove(move,color,moveWayList);
                    return true;
                }
                if (board.isPlayer(a+x,b+y,color)) {
                    return false;
                }
                if (board.isPlayer(a+x,b+y,-1)) {
                    moveWayList=null;
                    MoveWay moveWay1=new MoveWay(0,a,b,a+x,b+y);
                    MoveWay moveWay2=new MoveWay(0,k,l,a,b);
                    addMoveWay(moveWay1);
                    addMoveWay(moveWay2);
                    addMove(move,color,moveWayList);
                    return true;
                }
                if (n==2) return false;
                if (!board.exist(a+2*x,b+2*y)) {
                    moveWayList=null;
                    MoveWay moveWay1=new MoveWay(1,k,l,a,b);
                    MoveWay moveWay2=new MoveWay(0,k,l,a,b);
                    addMoveWay(moveWay1);
                    addMoveWay(moveWay2);
                    addMove(move,color,moveWayList);
                    return true;
                }
                if (board.isPlayer(a+2*x,b+2*y,-1)) {
                    moveWayList=null;
                    MoveWay moveWay1=new MoveWay(0,a,b,a+2*x,b+2*y);
                    MoveWay moveWay2=new MoveWay(0,k,l,a,b);
                    addMoveWay(moveWay1);
                    addMoveWay(moveWay2);
                    addMove(move,color,moveWayList);
                    return true;
                }
                return false;
            }
            return false;
        }
        moveWayList=null;
        for(int m=0;m<n;m++) {
            a=i+m*u+x;
            b=j+m*v+y;
            addMoveWay(new MoveWay(0,a-x,b-y,a,b));
            if (!( (board.exist(a,b)) && (board.isPlayer(a,b,-1)) )) {
                return false;
            }
        }
        addMove(move,color,moveWayList);
        return true;
    }

    private void findPossibles(int color,Board board) {
        possibles[color]=null;
        int[][] directions={{1, 0}, {0, 1}, {-1, 1}};
        for(int i=0;i<9;i++) {
            for(int j=0;j<9;j++) {
                if(board.isPlayer(i,j,color)) {
                    for(int n=1;n<4;n++) {
                        for(int a=0;a<3;a++) {
                            int u=directions[a][0];
                            int v=directions[a][1];
                            for(int b=0;b<3;b++) {
                                int x=directions[b][0];
                                int y=directions[b][1];
                                Move move1=new Move(i,j,n,u,v,x,y);
                                Move move2=new Move(i,j,n,u,v,-x,-y);
                                isPossible(color,move1);
                                isPossible(color,move2);							}
                        }
                    }
                }
            }
        }
    }

    public LinkedList<BallMove> play(Board board) {

        //MediaPlayer player;
        //player=MediaPlayer.create(this,R.raw.aba);
        //player.start();

        this.board=board;
        if (difficulty==1) {
            findPossibles(iaColor,board);
            //possibles[iaColor].display();
            MoveWayList moveWayList=bestMove(iaColor,board);
            Board boardTemp= new Board(board);
            board.doMoveList(moveWayList);
            return(board.differences(boardTemp,board));
        }
        if (difficulty==0) {
            findPossibles(iaColor,board);
            MoveList possiblesTemp=possibles[iaColor];
            MoveWayList moveWayListTemp=new MoveWayList();
            int valMax=-10000;
            while (possiblesTemp!=null) {
                MoveWayList moveWayList=possiblesTemp.getMoveWayList();
                Board boardTemp=new Board(board);
                boardTemp.doMoveList(moveWayList);
                findPossibles(1-iaColor,boardTemp);
                MoveWayList moveWayList2=bestMove(1-iaColor,boardTemp);
                boardTemp.doMoveList(moveWayList2);
                int valTemp=sum(iaColor,boardTemp);
                if (valTemp>valMax) {
                    valMax=valTemp;
                    moveWayListTemp=moveWayList;
                }
                possiblesTemp=possiblesTemp.getNext();
            }
            Board boardTemp= new Board(board);
            board.doMoveList(moveWayList);
            return(board.differences(boardTemp,board));
        }
        if (difficulty==3) {
            findPossibles(iaColor,board);
            MoveList possiblesTemp=possibles[iaColor];
            MoveWayList moveWayListTemp=new MoveWayList();
            int valMax=-10000;
            while (possiblesTemp!=null) {
                MoveWayList moveWayList=possiblesTemp.getMoveWayList();
                Board boardTemp=new Board(board);
                boardTemp.doMoveList(moveWayList);
                findPossibles(1-iaColor,boardTemp);
                MoveWayList moveWayList2=bestMove(1-iaColor,boardTemp);
                boardTemp.doMoveList(moveWayList2);
                findPossibles(iaColor,boardTemp);
                MoveWayList moveWayList3=bestMove(iaColor,boardTemp);
                boardTemp.doMoveList(moveWayList3);
                int valTemp=sum(iaColor,boardTemp);
                if (valTemp>valMax) {
                    valMax=valTemp;
                    moveWayListTemp=moveWayList;
                }
                possiblesTemp=possiblesTemp.getNext();
            }
            Board boardTemp= new Board(board);
            board.doMoveList(moveWayList);
            return(board.differences(boardTemp,board));
        }
        if (difficulty==2) {
            findPossibles(iaColor,board);
            MoveWayList moveWayList=bestMove2(iaColor,board);
            Board boardTemp= new Board(board);
            board.doMoveList(moveWayList);
            return(board.differences(boardTemp,board));
        }
        if (difficulty==4) {
            findPossibles(iaColor,board);
            MoveList possiblesTemp=possibles[iaColor];
            MoveWayList moveWayListTemp=new MoveWayList();
            int valMax=-10000;
            while (possiblesTemp!=null) {
                MoveWayList moveWayList=possiblesTemp.getMoveWayList();
                Board boardTemp=new Board(board);
                boardTemp.doMoveList(moveWayList);
                findPossibles(1-iaColor,boardTemp);
                MoveWayList moveWayList2=bestMove(1-iaColor,boardTemp);
                boardTemp.doMoveList(moveWayList2);
                findPossibles(iaColor,boardTemp);
                MoveWayList moveWayList3=bestMove2(iaColor,boardTemp);
                boardTemp.doMoveList(moveWayList3);
                int valTemp=sum(iaColor,boardTemp);
                if (valTemp>valMax) {
                    valMax=valTemp;
                    moveWayListTemp=moveWayList;
                }
                possiblesTemp=possiblesTemp.getNext();
            }
            Board boardTemp= new Board(board);
            board.doMoveList(moveWayList);
            return(board.differences(boardTemp,board));
        }
        return new LinkedList<>();
    }

    public MoveList getPossibles(int color) {
        return possibles[color];
    }
}
