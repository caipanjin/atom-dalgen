/*
 * Copyright (c) 2001, Aslak HellesÃ¸y, BEKK Consulting
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
package middlegen;

import javax.naming.*;
import javax.sql.*;
import java.sql.*;
import java.util.Hashtable;

/**
 * Describe what this class does
 *
 * @author Aslak Hellesøy
 * @created 21. april 2002
 * @todo-javadoc Write javadocs
 */
public class JNDIDatabase implements Database {

   /**
    * @todo-javadoc Describe the column
    */
   private final String _initialContextFactory;
   /**
    * @todo-javadoc Describe the column
    */
   private final String _providerURL;
   /**
    * @todo-javadoc Describe the column
    */
   private final String _dataSourceJNDIName;


   /**
    * Describe what the JNDIDatabase constructor does
    *
    * @todo-javadoc Write javadocs for constructor
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @param initialContextFactory Describe what the parameter does
    * @param providerURL Describe what the parameter does
    * @param dataSourceJNDIName Describe what the parameter does
    */
   public JNDIDatabase(String initialContextFactory, String providerURL, String dataSourceJNDIName) {
      if (initialContextFactory == null) {
         throw new IllegalArgumentException("initialContextFactory can't be null");
      }
      if (providerURL == null) {
         throw new IllegalArgumentException("providerURL can't be null");
      }
      if (dataSourceJNDIName == null) {
         throw new IllegalArgumentException("dataSourceJNDIName can't be null");
      }

      _initialContextFactory = initialContextFactory;
      _providerURL = providerURL;
      _dataSourceJNDIName = dataSourceJNDIName;
   }


   /**
    * Gets the Connection attribute of the JNDIDatabase object
    *
    * @todo-javadoc Write javadocs for exception
    * @return The Connection value
    * @exception MiddlegenException Describe the exception
    */
   public Connection getConnection() throws MiddlegenException {

      Context ctx = null;
      Hashtable ht = new Hashtable();
      ht.put(Context.INITIAL_CONTEXT_FACTORY, _initialContextFactory);
      ht.put(Context.PROVIDER_URL, _providerURL);

      try {
         ctx = new InitialContext(ht);
         // Use the context in your program
         DataSource ds = (DataSource)ctx.lookup(_dataSourceJNDIName);
         Connection connection = ds.getConnection();
         return connection;
      } catch (NoInitialContextException e) {
         e.printStackTrace();
         // a failure occurred
         throw new MiddlegenException("You should put your JNDI implementation classes on the system CLASSPATH:" + e.getMessage());
      } catch (CommunicationException e) {
         e.printStackTrace();
         // a failure occurred
         throw new MiddlegenException("Is your JNDI server running?:" + e.getMessage());
      } catch (NamingException e) {
         e.printStackTrace();
         // a failure occurred
         throw new MiddlegenException("Couldn't look up database using JNDI:" + e.getMessage());
      } catch (SQLException e) {
         // a failure occurred
         throw new MiddlegenException("Couldn't get Connection:" + e.getMessage());
      } finally {
         try {
            ctx.close();
         } catch (Exception e) {
            // a failure occurred
         }
      }
   }
}
