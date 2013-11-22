/*
 * Copyright (c) 2001, Aslak Hellesøy, BEKK Consulting
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of BEKK Consulting nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package middlegen.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import middlegen.RelationshipRole;

import com.atom.dalgen.utils.LogUtils;

/**
 * This class represents a relation line. Responsible for drawing the 2D stuff.
 *
 * @author <a href="mailto:aslak.hellesoy@bekk.no">Aslak Helles</a>
 * @created 3. oktober 2001
 * @version $Id: RelationLine.java,v 1.1 2005/10/25 14:59:22 lusu Exp $
 */
public class RelationLine {

    /**
     * @todo-javadoc Describe the column
     */
    private final Stroke           _regularStroke = new BasicStroke(2);

    /**
     * @todo-javadoc Describe the column
     */
    private final Stroke           _dashedStroke  = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] { 5, 5 }, 0);

    /**
     * @todo-javadoc Describe the column
     */
    private boolean                _isSelected;

    /**
     * @todo-javadoc Describe the column
     */
    //	private final Relation _relation;

    private final RelationshipRole _leftRole;
    /**
     * @todo-javadoc Describe the column
     */
    private final RelationshipRole _rightRole;

    /**
     * @todo-javadoc Describe the column
     */
    private final JTablePanel      _leftTable;
    /**
     * @todo-javadoc Describe the column
     */
    private final JTablePanel      _rightTable;

    /**
     * The lines from each key column to the main line on the western table
     */
    private final Line2D.Float[]   _leftLines;
    /**
     * The lines from each key column to the main line on the eastern table
     */
    private final Line2D.Float[]   _rightLines;
    /**
     * @todo-javadoc Describe the column
     */
    private final Line2D.Float     _mainLine;
    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _leftArrowPoint;
    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _rightArrowPoint;

    /**
     * @todo-javadoc Describe the column
     */
    private final Line2D.Float     _leftArrowLine1;
    /**
     * @todo-javadoc Describe the column
     */
    private final Line2D.Float     _leftArrowLine2;
    /**
     * @todo-javadoc Describe the column
     */
    private final Line2D.Float     _rightArrowLine1;
    /**
     * @todo-javadoc Describe the column
     */
    private final Line2D.Float     _rightArrowLine2;
    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _leftArrowPoint1;
    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _leftArrowPoint2;
    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _rightArrowPoint1;
    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _rightArrowPoint2;
    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _temp;
    /**
     * @todo-javadoc Describe the column
     */
    private final AffineTransform  _arrowTransform;

    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _leftCardinalityPoint;
    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _rightCardinalityPoint;

    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _leftFkPoint;
    /**
     * @todo-javadoc Describe the column
     */
    private final Point2D.Float    _rightFkPoint;
    /**
     * @todo-javadoc Describe the column
     */
    private double                 _mainLength;
    /**
     * @todo-javadoc Describe the column
     */
    private boolean                _leftIsWest;

    /** Average y between all key columns */
    private int                    _leftColumnY   = 0;

    /** Average y between all key columns */
    private int                    _rightColumnY  = 0;

    /**
     * @todo-javadoc Describe the field
     */
    private int[]                  _leftEdgeY;
    /**
     * @todo-javadoc Describe the field
     */
    private int[]                  _rightEdgeY;

    /**
     * @todo-javadoc Describe the column
     */
    private static Image           _star          = new ImageIcon(RelationLine.class.getResource("star.gif")).getImage();
    /**
     * @todo-javadoc Describe the column
     */
    private static Image           _one           = new ImageIcon(RelationLine.class.getResource("one.gif")).getImage();

    /**
     * @todo-javadoc Describe the column
     */
    private static Image           _star_white    = new ImageIcon(RelationLine.class.getResource("star_white.gif")).getImage();
    /**
     * @todo-javadoc Describe the column
     */
    private static Image           _one_white     = new ImageIcon(RelationLine.class.getResource("one_white.gif")).getImage();

    /**
     * @todo-javadoc Describe the column
     */
    private static Image           _fk            = new ImageIcon(RelationLine.class.getResource("fk.gif")).getImage();

    /**
     * @todo-javadoc Describe the column
     */
    private static RenderingHints  _renderHints;

    /**
     * Creates new RelationLine
     *
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @param leftRole Describe what the parameter does
     * @param rightRole Describe what the parameter does
     * @param leftTable Describe what the parameter does
     * @param rightTable Describe what the parameter does
     */
    public RelationLine(RelationshipRole leftRole, RelationshipRole rightRole, JTablePanel leftTable, JTablePanel rightTable) {
        LogUtils.get().debug("new RelationLine for " + leftTable.getTable().getSqlName() + "-" + rightTable.getTable().getSqlName());
        _leftRole = leftRole;
        _rightRole = rightRole;
        _leftTable = leftTable;
        _rightTable = rightTable;

        // left table points
        LogUtils.get().debug("A:" + _leftRole.getColumnMaps().length);
        _leftEdgeY = new int[_leftRole.getColumnMaps().length];
        for (int i = 0; i < _leftEdgeY.length; i++) {
            _leftEdgeY[i] = _leftTable.getColumnY(_leftRole.getColumnMaps()[i].getPrimaryKey());
            _leftColumnY += _leftEdgeY[i];
        }
        _leftColumnY = _leftColumnY / _leftEdgeY.length;

        // right table points
        if (!_leftRole.getRelation().isMany2Many()) {
            LogUtils.get().debug("M:" + _leftRole.getColumnMaps().length);
            _rightEdgeY = new int[_leftRole.getColumnMaps().length];
            for (int i = 0; i < _rightEdgeY.length; i++) {
                _rightEdgeY[i] = _rightTable.getColumnY(_leftRole.getColumnMaps()[i].getForeignKey());
                _rightColumnY += _rightEdgeY[i];
            }
            _rightColumnY = _rightColumnY / _rightEdgeY.length;
        } else {
            LogUtils.get().debug("B:" + _rightRole.getColumnMaps().length);
            _rightEdgeY = new int[_rightRole.getColumnMaps().length];
            for (int i = 0; i < _rightEdgeY.length; i++) {
                _rightEdgeY[i] = _rightTable.getColumnY(_rightRole.getColumnMaps()[i].getPrimaryKey());
                _rightColumnY += _rightEdgeY[i];
            }
            _rightColumnY = _rightColumnY / _rightRole.getColumnMaps().length;
        }

        _leftLines = createLines(_leftEdgeY.length);
        _rightLines = createLines(_rightEdgeY.length);

        _mainLine = new Line2D.Float();

        _leftCardinalityPoint = new Point2D.Float();
        _rightCardinalityPoint = new Point2D.Float();

        _leftFkPoint = new Point2D.Float();
        _rightFkPoint = new Point2D.Float();

        _leftArrowPoint = new Point2D.Float();
        _rightArrowPoint = new Point2D.Float();

        _leftArrowLine1 = new Line2D.Float();
        _leftArrowLine2 = new Line2D.Float();
        _rightArrowLine1 = new Line2D.Float();
        _rightArrowLine2 = new Line2D.Float();
        _leftArrowPoint1 = new Point2D.Float();
        _leftArrowPoint2 = new Point2D.Float();
        _rightArrowPoint1 = new Point2D.Float();
        _rightArrowPoint2 = new Point2D.Float();
        _temp = new Point2D.Float();
        _arrowTransform = new AffineTransform();

    }

    /**
     * Sets the Selected attribute of the RelationLine object
     *
     * @param isSelected The new Selected value
     */
    public void setSelected(boolean isSelected) {
        _isSelected = isSelected;
    }

    /**
     * Gets the Selected attribute of the RelationLine object
     *
     * @return The Selected value
     */
    public boolean isSelected() {
        return _isSelected;
    }

    /**
     * Updates all coordinates
     *
     * @todo Optimise. Some info can be cached
     * @todo Handle m:n (leftY/rightY etc)
     */
    public void update() {
        /*
         *  int leftY = _leftTable.getY() + _leftTable.getColumnY(_leftRole.getColumnMap().getPrimaryKey());
         *  int rightY;
         *  if (!_leftRole.getRelation().isMany2Many()) {
         *  rightY = _rightTable.getY() + _rightTable.getColumnY(_leftRole.getColumnMap().getForeignKey());
         *  }
         *  else {
         *  rightY = _rightTable.getY() + _rightTable.getColumnY(_rightRole.getColumnMap().getPrimaryKey());
         *  }
         */
        int leftY = _leftTable.getY() + _leftColumnY;
        int rightY = _rightTable.getY() + _rightColumnY;

        // find out which table is farthest west (and east)
        JTablePanel westTable;
        JTablePanel eastTable;
        int westY;
        int eastY;
        Line2D.Float[] westLines;
        Line2D.Float[] eastLines;
        int[] westEdgeY;
        int[] eastEdgeY;

        _leftIsWest = _leftTable.getX() < _rightTable.getX();

        if (_leftIsWest) {
            westTable = _leftTable;
            eastTable = _rightTable;
            westY = leftY;
            eastY = rightY;
            westLines = _leftLines;
            eastLines = _rightLines;
            westEdgeY = _leftEdgeY;
            eastEdgeY = _rightEdgeY;
        } else {
            westTable = _rightTable;
            eastTable = _leftTable;
            westY = rightY;
            eastY = leftY;
            westLines = _rightLines;
            eastLines = _leftLines;
            westEdgeY = _rightEdgeY;
            eastEdgeY = _leftEdgeY;
        }

        // find out whether the tables are more or less vertically aligned
        boolean aligned = (eastTable.getX() - westTable.getX()) < eastTable.getWidth() / 2;
        // TODO: handle different widths

        int westX;
        if (aligned) {
            westX = westTable.getX() - 10;
            for (int i = 0; i < westEdgeY.length; i++) {
                westLines[i].setLine(westX, westY, westTable.getX(), westEdgeY[i] + westTable.getY());
            }
        } else {
            westX = westTable.getX() + westTable.getWidth() + 10;
            for (int i = 0; i < westEdgeY.length; i++) {
                westLines[i].setLine(westX, westY, westTable.getX() + westTable.getWidth(), westEdgeY[i] + westTable.getY());
            }
        }
        int eastX = eastTable.getX() - 10;

        for (int i = 0; i < eastEdgeY.length; i++) {
            eastLines[i].setLine(eastX, eastY, eastTable.getX(), eastEdgeY[i] + eastTable.getY());
        }

        _mainLine.setLine(westX, westY, eastX, eastY);

        setCardinalityPoints();
    }

    /**
     * Describe what the method does
     *
     * @todo-javadoc Write javadocs for method
     * @todo-javadoc Write javadocs for method parameter
     * @param g Describe what the parameter does
     */
    public void paint(Graphics2D g) {
        g.setRenderingHints(_renderHints);
        /*
         *  if( isSelected() ) {
         *  g.setColor( Color.red );
         *  } else {
         *  g.setColor( Color.black );
         *  }
         */
        if (_leftRole.getRelation().isMany2Many()) {
            g.setStroke(_dashedStroke);
        } else {
            g.setStroke(_regularStroke);
        }
        drawLineUnlessItsLengthIsZero(g, _mainLine);

        g.setStroke(_regularStroke);
        for (int i = 0; i < _leftLines.length; i++) {
            drawLineUnlessItsLengthIsZero(g, _leftLines[i]);
        }
        for (int i = 0; i < _rightLines.length; i++) {
            drawLineUnlessItsLengthIsZero(g, _rightLines[i]);
        }
        drawImage(g, getDisplayImage(_rightRole, g.getColor()), _leftCardinalityPoint.x - 8, _leftCardinalityPoint.y - 8);
        drawImage(g, getDisplayImage(_leftRole, g.getColor()), _rightCardinalityPoint.x - 8, _rightCardinalityPoint.y - 8);

        if (_rightRole.isEnabled()) {
            drawLineUnlessItsLengthIsZero(g, _leftArrowLine1);
            drawLineUnlessItsLengthIsZero(g, _leftArrowLine2);
        }
        if (_leftRole.isEnabled()) {
            drawLineUnlessItsLengthIsZero(g, _rightArrowLine1);
            drawLineUnlessItsLengthIsZero(g, _rightArrowLine2);
        }

        // draw the fk image
        if (!_leftRole.isOriginPrimaryKey()) {
            drawImage(g, _fk, _leftFkPoint.x - 6, _leftFkPoint.y - 6);
        }
        if (!_rightRole.isOriginPrimaryKey()) {
            drawImage(g, _fk, _rightFkPoint.x - 6, _rightFkPoint.y - 6);
        }
    }

    /**
     * @todo Refactor this. Shouldn't mix localisation logic and relation
     *      mutation logic in one method
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for return value
     * @todo-javadoc Write javadocs for method parameter
     * @param evt Describe what the parameter does
     * @return Describe the return value
     */
    public boolean selectMaybe(MouseEvent evt) {
        boolean multiplicity = evt.isControlDown();
        boolean cardinality = evt.isShiftDown();
        Point point = evt.getPoint();
        setSelected(_mainLine.intersects(point.getX() - 5, point.getY() - 5, 10, 10));
        if (isSelected()) {
            double distFromP1 = Point2D.distance(_mainLine.getX1(), _mainLine.getY1(), point.getX(), point.getY());

            boolean nearP1 = distFromP1 < _mainLength / 2;
            boolean leftChosen = (_leftIsWest && nearP1) || (!_leftIsWest && !nearP1);
            RelationshipRole role = leftChosen ? _rightRole : _leftRole;
            if (multiplicity) {
                // toggle multiplicity. Let's prevent the user from doing something stupid. Some cardinalities
                // can't be changed. we know best etc.
                if (role.getRelation().isMany2Many()) {
                    // don't allow to change cardinality of m:n relationships
                    JOptionPane.showMessageDialog(null, "Can't change the cardinality of a many-to-many relationship", "Cardinality", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                if (role.isFkPk()) {
                    // don't allow to change cardinality of 1:1 relationships that are 1:1 because fk is also pk
                    JOptionPane.showMessageDialog(null, "Can't change the cardinality of a one-to-one relationship where the foreign key is also a primary key", "Cardinality",
                        JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                if (role.isTargetPrimaryKey()) {
                    JOptionPane.showMessageDialog(null, "Can't change the cardinality of a the one-side of a relationship that corresponds to a primary key", "Cardinality",
                        JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                // All checks passed. Do the toggle
                role.setTargetMany(!role.isTargetMany());
            }
            if (cardinality) {
                // toggle between uni/bidirectional
                role.setEnabled(!role.isEnabled());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the CardinalityPoints attribute of the RelationLine object
     *
     * @todo refactor this duplicate code!!!
     */
    private void setCardinalityPoints() {
        // compute vector of length 1
        _mainLength = Point2D.distance(_mainLine.getX1(), _mainLine.getY1(), _mainLine.getX2(), _mainLine.getY2());
        double vx = (_mainLine.getX2() - _mainLine.getX1()) / _mainLength;
        double vy = (_mainLine.getY2() - _mainLine.getY1()) / _mainLength;
        double cardx = 20.0 * vx;
        double cardy = 20.0 * vy;
        double arrowx = 30.0 * vx;
        double arrowy = 30.0 * vy;
        if (_leftIsWest) {
            _leftArrowPoint.setLocation(_mainLine.getX1() + cardx, _mainLine.getY1() + cardy);
            _rightArrowPoint.setLocation(_mainLine.getX2() - cardx, _mainLine.getY2() - cardy);

            // The left-side arrow head
            _temp.setLocation(_mainLine.getX1() + arrowx, _mainLine.getY1() + arrowy);

            _arrowTransform.setToRotation(Math.PI / 6, _leftArrowPoint.getX(), _leftArrowPoint.getY());
            _arrowTransform.transform(_temp, _leftArrowPoint1);
            _leftArrowLine1.setLine(_leftArrowPoint.getX(), _leftArrowPoint.getY(), _leftArrowPoint1.getX(), _leftArrowPoint1.getY());

            _arrowTransform.setToRotation(-Math.PI / 6, _leftArrowPoint.getX(), _leftArrowPoint.getY());
            _arrowTransform.transform(_temp, _leftArrowPoint2);
            _leftArrowLine2.setLine(_leftArrowPoint.getX(), _leftArrowPoint.getY(), _leftArrowPoint2.getX(), _leftArrowPoint2.getY());

            _arrowTransform.setToRotation(Math.PI / 2, _leftArrowPoint.getX(), _leftArrowPoint.getY());
            _arrowTransform.transform(_temp, _leftCardinalityPoint);

            _arrowTransform.setToRotation(-Math.PI / 2, _leftArrowPoint.getX(), _leftArrowPoint.getY());
            _arrowTransform.transform(_temp, _leftFkPoint);

            // The right-side arrow head
            _temp.setLocation(_mainLine.getX2() - arrowx, _mainLine.getY2() - arrowy);

            _arrowTransform.setToRotation(Math.PI / 6, _rightArrowPoint.getX(), _rightArrowPoint.getY());
            _arrowTransform.transform(_temp, _rightArrowPoint1);
            _rightArrowLine1.setLine(_rightArrowPoint.getX(), _rightArrowPoint.getY(), _rightArrowPoint1.getX(), _rightArrowPoint1.getY());

            _arrowTransform.setToRotation(-Math.PI / 6, _rightArrowPoint.getX(), _rightArrowPoint.getY());
            _arrowTransform.transform(_temp, _rightArrowPoint2);
            _rightArrowLine2.setLine(_rightArrowPoint.getX(), _rightArrowPoint.getY(), _rightArrowPoint2.getX(), _rightArrowPoint2.getY());

            _arrowTransform.setToRotation(-Math.PI / 2, _rightArrowPoint.getX(), _rightArrowPoint.getY());
            _arrowTransform.transform(_temp, _rightCardinalityPoint);

            _arrowTransform.setToRotation(Math.PI / 2, _rightArrowPoint.getX(), _rightArrowPoint.getY());
            _arrowTransform.transform(_temp, _rightFkPoint);
        } else {
            _leftArrowPoint.setLocation(_mainLine.getX2() - cardx, _mainLine.getY2() - cardy);
            _rightArrowPoint.setLocation(_mainLine.getX1() + cardx, _mainLine.getY1() + cardy);

            // The left-side arrow head
            _temp.setLocation(_mainLine.getX2() - arrowx, _mainLine.getY2() - arrowy);

            _arrowTransform.setToRotation(Math.PI / 6, _leftArrowPoint.getX(), _leftArrowPoint.getY());
            _arrowTransform.transform(_temp, _leftArrowPoint1);
            _leftArrowLine1.setLine(_leftArrowPoint.getX(), _leftArrowPoint.getY(), _leftArrowPoint1.getX(), _leftArrowPoint1.getY());

            _arrowTransform.setToRotation(-Math.PI / 6, _leftArrowPoint.getX(), _leftArrowPoint.getY());
            _arrowTransform.transform(_temp, _leftArrowPoint2);
            _leftArrowLine2.setLine(_leftArrowPoint.getX(), _leftArrowPoint.getY(), _leftArrowPoint2.getX(), _leftArrowPoint2.getY());

            _arrowTransform.setToRotation(Math.PI / 2, _leftArrowPoint.getX(), _leftArrowPoint.getY());
            _arrowTransform.transform(_temp, _leftCardinalityPoint);

            _arrowTransform.setToRotation(-Math.PI / 2, _leftArrowPoint.getX(), _leftArrowPoint.getY());
            _arrowTransform.transform(_temp, _leftFkPoint);

            // The right-side arrow head
            _temp.setLocation(_mainLine.getX1() + arrowx, _mainLine.getY1() + arrowy);

            _arrowTransform.setToRotation(Math.PI / 6, _rightArrowPoint.getX(), _rightArrowPoint.getY());
            _arrowTransform.transform(_temp, _rightArrowPoint1);
            _rightArrowLine1.setLine(_rightArrowPoint.getX(), _rightArrowPoint.getY(), _rightArrowPoint1.getX(), _rightArrowPoint1.getY());

            _arrowTransform.setToRotation(-Math.PI / 6, _rightArrowPoint.getX(), _rightArrowPoint.getY());
            _arrowTransform.transform(_temp, _rightArrowPoint2);
            _rightArrowLine2.setLine(_rightArrowPoint.getX(), _rightArrowPoint.getY(), _rightArrowPoint2.getX(), _rightArrowPoint2.getY());

            _arrowTransform.setToRotation(-Math.PI / 2, _rightArrowPoint.getX(), _rightArrowPoint.getY());
            _arrowTransform.transform(_temp, _rightCardinalityPoint);

            _arrowTransform.setToRotation(Math.PI / 2, _rightArrowPoint.getX(), _rightArrowPoint.getY());
            _arrowTransform.transform(_temp, _rightFkPoint);
        }
    }

    /**
     * Gets the cardinality Image to display. It's a 1 or a *, black or blue.
     *
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @param role Describe what the parameter does
     * @param c Describe what the parameter does
     * @return The LeftDisplayString value
     */
    private Image getDisplayImage(RelationshipRole role, Color c) {
        // This is quite dirty, but it works
        if (c == Color.white) {
            return role.isTargetMany() ? _star_white : _one_white;
        } else {
            return role.isTargetMany() ? _star : _one;
        }
    }

    /**
     * Describe what the method does
     *
     * @todo-javadoc Write javadocs for method
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @param g Describe what the parameter does
     * @param i Describe what the parameter does
     * @param x Describe what the parameter does
     * @param y Describe what the parameter does
     */
    private final void drawImage(final Graphics2D g, final Image i, final float x, final float y) {
        if (!Float.isNaN(x)) {
            g.drawImage(i, (int) x, (int) y, null);
        }
    }

    /**
     * Describe what the method does
     *
     * @todo-javadoc Write javadocs for method
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @param g Describe what the parameter does
     * @param line Describe what the parameter does
     */
    private final void drawLineUnlessItsLengthIsZero(final Graphics2D g, final Line2D line) {
        // Any coord is NaN if the length is zero
        if (!Double.isNaN(line.getP1().getX())) {
            g.draw(line);
        }
    }

    /**
     * Describe what the method does
     *
     * @todo-javadoc Write javadocs for method
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for return value
     * @param n Describe what the parameter does
     * @return Describe the return value
     */
    private static Line2D.Float[] createLines(int n) {
        Line2D.Float[] result = new Line2D.Float[n];
        for (int i = 0; i < n; i++) {
            result[i] = new Line2D.Float();
        }
        return result;
    }

    static {
        _renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        _renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}
