package models;

public enum Status {
    PENDING {
        @Override
        public String toString(){
            return "Pending";
        }
    },

    APPROVED {
        @Override
        public String toString(){
            return "Approved";
        }
    },

    DENIED {
        @Override
        public String toString(){
            return "Denied";
        }
    }
}
