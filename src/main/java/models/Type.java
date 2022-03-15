package models;

public enum Type {
    LODGING {
        @Override
        public String toString(){
            return "Lodging";
        }
    },

    FOOD {
        @Override
        public String toString(){
            return "Food";
        }
    },

    TRAVEL {
        @Override
        public String toString(){
            return "Travel";
        }
    }
}
