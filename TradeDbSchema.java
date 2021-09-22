package com.bignerdranch.android.tradesexplorer20;

public class TradeDbSchema {
    public static final class AreasTable {
        public static final String NAME = "area";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String XVALUE = "xvalue";
            public static final String YVALUE = "yvalue";
            public static final String DESC = "desc";
            public static final String TOWN = "town";
            public static final String EXPLORED = "explored";
            public static final String STARRED = "starred";
        }
    }

    public static final class ItemsTable {
        public static final String NAME = "item";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TYPE = "type";
            public static final String TITLE = "title";
            public static final String DESC = "description";
            public static final String VALUE = "value";
            public static final String UNIQUE = "uniqueValue";
        }
    }

    public static final class PlayerTable {
        public static final String NAME = "player";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String ROW = "row";
            public static final String COL = "col";
            public static final String CASH = "cash";
            public static final String HEALTH = "health";
            public static final String EQMASS = "equipmentmass";
        }
    }
}
