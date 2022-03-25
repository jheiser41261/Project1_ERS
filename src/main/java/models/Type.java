package models;

public enum Type {
    LODGING(1) {
        @Override
        public String toString(){
            return "Lodging";
        }
    },

    FOOD(2) {
        @Override
        public String toString(){
            return "Food";
        }
    },

    TRAVEL(3) {
        @Override
        public String toString(){
            return "Travel";
        }
    };

    private Integer reimbType;

    Type(Integer reimbType){
        this.reimbType = reimbType;
    }

    public Integer getReimbType() {
        return reimbType;
    }
}
