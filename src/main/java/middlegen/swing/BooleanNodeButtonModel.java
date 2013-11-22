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

import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import middlegen.util.BooleanNode;

/**
 * Describe what this class does
 *
 * @author Aslak Helles
 * @created 26. mars 2002
 * @todo-javadoc Write javadocs
 */
public class BooleanNodeButtonModel extends JToggleButton.ToggleButtonModel implements Observer {
   /**
    * @todo-javadoc Describe the column
    */
   private final BooleanNode _booleanNode;


   /**
    * Describe what the BooleanNodeButtonModel constructor does
    *
    * @todo-javadoc Write javadocs for constructor
    * @todo-javadoc Write javadocs for method parameter
    * @param booleanNode Describe what the parameter does
    */
   public BooleanNodeButtonModel(BooleanNode booleanNode) {
      if (booleanNode == null) {
         throw new IllegalArgumentException("booleanNode can't be null");
      }
      _booleanNode = booleanNode;
      _booleanNode.addObserver(this);
   }


   /**
    * Sets the Selected attribute of the BooleanNodeButtonModel object
    *
    * @param b The new Selected value
    */
   public void setSelected(boolean b) {
      _booleanNode.setValue(b);
   }


   /**
    * Gets the Selected attribute of the BooleanNodeButtonModel object
    *
    * @return The Selected value
    */
   public boolean isSelected() {
      return _booleanNode.isPartiallyTrue();
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @param o Describe what the parameter does
    * @param arg Describe what the parameter does
    */
   public void update(Observable o, Object arg) {
      fireStateChanged();
   }
}
