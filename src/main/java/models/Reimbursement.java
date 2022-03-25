package models;

import java.util.Date;

public class Reimbursement {
    private Integer id;
    private Double amount;
    private Date submitted;
    private Date resolved;
    private String description;
    private String author;
    private String resolver;
    private String status;
    private String type;

    public Reimbursement() {
    }

    public Reimbursement(Integer id, String author) {
        this.id = id;
        this.author = author;
    }

    public Reimbursement(Double amount, String description, String type) {
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    public Reimbursement(Double amount, String description, String author, String type) {
        this.amount = amount;
        this.description = description;
        this.author = author;
        this.type = type;
    }

    public Reimbursement(Integer id, Double amount, Date submitted, Date resolved, String description, String author, String resolver, String status, String type) {
        this.id = id;
        this.amount = amount;
        this.submitted = submitted;
        this.resolved = resolved;
        this.description = description;
        this.author = author;
        this.resolver = resolver;
        this.status = status;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }

    public Date getResolved() {
        return resolved;
    }

    public void setResolved(Date resolved) {
        this.resolved = resolved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "id=" + id +
                ", amount=" + amount +
                ", submitted=" + submitted +
                ", resolved=" + resolved +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", resolver=" + resolver +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
