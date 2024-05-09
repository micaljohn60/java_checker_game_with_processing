package Checkers;

public class CheckersPiece {
    private Cell location;

    /**
     * w=White piece, b=black piece
     */
    private char color;

    private boolean isKing = false;

    public boolean isKing() {
        return this.isKing;
    }

    public void pormoteToKing() {
        this.isKing = true;
    }

    public CheckersPiece(char color) {
        this.color = color;
    }

    public char getColor() {
        return this.color;
    }

    public void setLocation(Cell c) {
        this.location = c;
    }

    public void draw(App app) {
        app.strokeWeight(3f);
        if (this.getColor() == 'w') {
            app.fill(255);
            app.stroke(0);
        } else if (this.getColor() == 'b') {
            app.fill(0);
            app.stroke(255);
        }

        app.ellipse(location.getX() * App.CELLSIZE + App.CELLSIZE / 2,
                location.getY() * App.CELLSIZE + App.CELLSIZE / 2,
                App.CELLSIZE * 0.8f,
                App.CELLSIZE * 0.8f);

        if (isKing) {
            if(this.getColor() == 'w')
            {
                app.fill(255);
                app.stroke(0);
                app.ellipse(location.getX() * App.CELLSIZE + App.CELLSIZE / 2,
                        location.getY() * App.CELLSIZE + App.CELLSIZE / 2,
                        App.CELLSIZE * 0.8f,
                        App.CELLSIZE * 0.8f);

                app.fill(255);
                app.stroke(0);
                app.ellipse(location.getX() * App.CELLSIZE + App.CELLSIZE / 2,
                        location.getY() * App.CELLSIZE + App.CELLSIZE / 2,
                        App.CELLSIZE * 0.4f,
                        App.CELLSIZE * 0.4f);
            }
            else {
                app.fill(0);
                app.stroke(255);
                app.ellipse(location.getX() * App.CELLSIZE + App.CELLSIZE / 2,
                        location.getY() * App.CELLSIZE + App.CELLSIZE / 2,
                        App.CELLSIZE * 0.8f,
                        App.CELLSIZE * 0.8f);
//
                app.fill(0);
                app.stroke(255);
                app.ellipse(location.getX() * App.CELLSIZE + App.CELLSIZE / 2,
                        location.getY() * App.CELLSIZE + App.CELLSIZE / 2,
                        App.CELLSIZE * 0.4f,
                        App.CELLSIZE * 0.4f);
            }
            // You can adjust the size and position of the crown to fit your preference
        }
    }

    @Override
    public String toString() {
        return "CheckersPiece{" +
                "location=" + location + ", color=" + color +
                '}';
    }
}
