/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.examples.petstore;

public class PetStore {

    public static void main(String[] args) {
        args = new String[] {
                "-database",
                "petstore",
                "-config",
                "sql/lealone.sql",
                // "sql/llm.sql",
                "sql/tables.sql",
                "sql/init-data.sql",
                "sql/services.sql" };
        com.lealone.main.Lealone.main(args);
    }
}
