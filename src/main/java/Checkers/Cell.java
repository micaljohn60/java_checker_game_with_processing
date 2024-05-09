package Checkers;

public class Cell {

    private int x;
    private int y;

    private int status = 0;

    private CheckersPiece piece;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPiece(CheckersPiece p) {
        if(p == null)
        {
            this.piece = null;
        }
        else{
            this.piece = p;
            p.setLocation(this);
        }

    }



    public CheckersPiece getPiece() {
        return this.piece;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return this.y;
    }

    public void setStatus(int c) {
        this.status = c;
    }

    public void draw(App app) {
        app.noStroke();
        if (app.getSelectedCell() == this) {
            app.setFill(1, (x+y)%2);
        } else {
            app.setFill(0, (x+y)%2);
        }
        app.rect(x*App.CELLSIZE, y*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
        if (this.piece != null) piece.draw(app);
    }

    @Override
    public String toString() {
        return this.x + " : " + this.y;
    }
}
