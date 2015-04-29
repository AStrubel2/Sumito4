package axelindustry.sumito4.IA;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by Clement on 08/04/2015.
 */
public class Board {

    private int[][] matrice;
    private Move[] userMove;
    private int colorTemp;

    public Board() {
        matrice=new int[9][9];
        for(int i=0;i<9;i++) {
            for(int j=0;j<9;j++) {
                matrice[i][j]=-2;
            }
        }
        for(int j=4;j<9;j++)
            matrice[0][j]=0;
        for(int j=3;j<9;j++)
            matrice[1][j]=0;
        for(int j=2;j<9;j++)
            matrice[2][j]=-1;
        for(int j=4;j<7;j++)
            matrice[2][j]=0;
        for(int j=1;j<9;j++)
            matrice[3][j]=-1;
        for(int j=0;j<9;j++)
            matrice[4][j]=-1;
        for(int j=0;j<8;j++)
            matrice[5][j]=-1;
        for(int j=0;j<7;j++)
            matrice[6][j]=-1;
        for(int j=2;j<5;j++)
            matrice[6][j]=1;
        for(int j=0;j<6;j++)
            matrice[7][j]=1;
        for(int j=0;j<5;j++)
            matrice[8][j]=1;
    }

    public Board(Board board) {
        matrice=new int[9][9];
        for(int i=0;i<9;i++) {
            for(int j=0;j<9;j++) {
                this.matrice[i][j]=board.get(i,j);
            }
        }
    }

    public int get(int i,int j) {
        return matrice[i][j];
    }

    private String convert(int n) {
        if(n==1) return "*";
        if(n==0) return "^";
        if(n==-1) return "°";
        return "";
    }

    public void display() {
        for(int i=0;i<4;i++)
            Log.d("test"," ");
        for(int i=4;i<9;i++) {
            Log.d("test",convert(matrice[0][i]));
            Log.d("test"," ");
        }
        Log.d("test","\n");
        for(int i=0;i<3;i++)
            Log.d("test"," ");
        for(int i=3;i<9;i++) {
            Log.d("test",convert(matrice[1][i]));
            Log.d("test"," ");
        }
        Log.d("test","\n");
        for(int i=0;i<2;i++)
            Log.d("test"," ");
        for(int i=2;i<9;i++) {
            Log.d("test",convert(matrice[2][i]));
            Log.d("test"," ");
        }
        Log.d("test","\n");
        for(int i=0;i<1;i++)
            Log.d("test"," ");
        for(int i=1;i<9;i++) {
            Log.d("test",convert(matrice[3][i]));
            Log.d("test"," ");
        }
        Log.d("test","\n");
        for(int i=0;i<9;i++) {
            Log.d("test",convert(matrice[4][i]));
            Log.d("test"," ");
        }
        Log.d("test","\n");
        for(int i=0;i<1;i++)
            Log.d("test"," ");
        for(int i=0;i<8;i++) {
            Log.d("test",convert(matrice[5][i]));
            Log.d("test"," ");
        }
        Log.d("test","\n");
        for(int i=0;i<2;i++)
            Log.d("test"," ");
        for(int i=0;i<7;i++) {
            Log.d("test",convert(matrice[6][i]));
            Log.d("test"," ");
        }
        Log.d("test","\n");
        for(int i=0;i<3;i++)
            Log.d("test"," ");
        for(int i=0;i<6;i++) {
            Log.d("test",convert(matrice[7][i]));
            Log.d("test"," ");
        }
        Log.d("test","\n");
        for(int i=0;i<4;i++)
            Log.d("test"," ");
        for(int i=0;i<5;i++) {
            Log.d("test",convert(matrice[8][i]));
            Log.d("test"," ");
        }
        Log.d("test","\n");
        Log.d("test","\n");
    }

    public Boolean exist(int i,int j) {
        if ((i>=0)&&(i<9)&&(j>=0)&&(j<9)) {
            return (matrice[i][j] != -2);
        }
        return false;
    }

    public Boolean isPlayer(int i,int j,int iaColor) {
        if (exist(i,j)) {
            return (matrice[i][j]==iaColor);
        }
        return false;
    }

    public void doMove(MoveWay moveWay) {
        int type=moveWay.type;
        int a=moveWay.a;
        int b=moveWay.b;
        int x=moveWay.x;
        int y=moveWay.y;
        if (type==0) {
            matrice[a][b]=matrice[x][y];
            matrice[x][y]=-1;
            return;
        }
        if (type==1) {
            matrice[a][b]=-1;
        }
    }

    public void doMoveList(MoveWayList moveWayList) {
        if (moveWayList==null) return;
        doMove(moveWayList.getMoveWay());
        doMoveList(moveWayList.getNext());
    }

    public LinkedList<Ball> getBalls() {
        LinkedList<Ball> ballList=new LinkedList<>();
        for(int i=0;i<9;i++) {
            for(int j=0;j<9;j++) {
                if (matrice[i][j]>=0) {
                    ballList.add(new Ball(matrice[i][j],i,j));
                }
            }
        }
        return ballList;
    }

    public Boolean[] getDirections(int i,int j) {
        userMove=new Move[6];
        Boolean [] list=new Boolean[6];
        int color=matrice[i][j];
        colorTemp=color;
        Bot bot=new Bot(0,0,0,this);
        int[] i0={0,-1,-1,0,1,1};
        int[] j0={1,1,0,-1,-1,0};
        for(int k=0;k<6;k++) {
            Move move=new Move(i,j,1,0,0,i0[k],j0[k]);
            list[k]=bot.isPossible(color,move);
            userMove[k]=move;
        }
        return list;
    }

    public Boolean[] getDirections(int i1,int j1,int i2,int j2) {
        userMove=new Move[6];
        Boolean [] list=new Boolean[6];
        int color=matrice[i1][j1];
        colorTemp=color;
        Bot bot=new Bot(0,0,0,this);
        int x=i2-i1;
        int y=j2-j1;
        int[] i0={0,-1,-1,0,1,1};
        int[] j0={1,1,0,-1,-1,0};
        for(int k=0;k<6;k++) {
            Move move=new Move(i1,j1,2,x,y,i0[k],j0[k]);
            list[k]=bot.isPossible(color,move);
            userMove[k]=move;
        }
        return list;
    }

    public Boolean[] getDirections(int i1,int j1,int i2,int j2,int i3,int j3) {
        userMove=new Move[6];
        Boolean [] list=new Boolean[6];
        int color=matrice[i1][j1];
        colorTemp=color;
        Bot bot=new Bot(0,0,0,this);
        int x;
        int y;
        int i;
        int j;
        if ((i2-i1==i3-i2)&&(j2-j1==j3-j2)) {
            i=i1;
            j=j1;
            x=i2-i1;
            y=j2-j1;
        }
        else if ((i3-i1==i2-i3)&&(j3-j1==j2-j3)) {
            i=i1;
            j=j1;
            x=i3-i1;
            y=j3-j1;
        }
        else {
            i=i2;
            j=j2;
            x=i1-i2;
            y=j1-j2;
        }
        int[] i0={0,-1,-1,0,1,1};
        int[] j0={1,1,0,-1,-1,0};
        for(int k=0;k<6;k++) {
            Move move=new Move(i,j,3,x,y,i0[k],j0[k]);
            list[k]=bot.isPossible(color,move);
            userMove[k]=move;
        }
        return list;
    }

    public void doUserMove(int angle) {
        Move move=userMove[angle];
        Bot bot=new Bot(0,0,0,this);
        if (bot.isPossible(colorTemp,move)) {
            MoveWayList moveWayList=bot.getPossibles(colorTemp).getMoveWayList();
            doMoveList(moveWayList);
        }
    }
}
