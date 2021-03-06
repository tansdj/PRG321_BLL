/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductManagement;

import PersonManagement.Address;
import PersonManagement.Contact;
import PersonManagement.Department;
import PersonManagement.Person;
import PersonManagement.User;
import bc_stationary_bll.Datahandling;
import bc_stationary_dll.Datahandler;
import bc_stationary_dll.Datahelper;
import bc_stationary_dll.TableSpecifiers;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tanya This class represents objects that are used to save client
 * request. Therefore, a client send a request for stationary, if it is
 * approved, it becomes an order, if not the request will simply be closed.
 */
public class UserRequest implements Datahandling, Serializable {

    private User user;
    private Product product;
    private int quantity;
    public int priorityLevel;
    private String status;
    private Date reqDate;
    private Date completedDate;

    public UserRequest() {
    }

    public UserRequest(User user, Product product, int quantity, int priorityLevel, String status, Date reqDate, Date completedDate) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.priorityLevel = priorityLevel;
        this.status = status;
        this.reqDate = reqDate;
        this.completedDate = completedDate;
    }

    public UserRequest(User user) {
        this.user = user;
    }

    public UserRequest(Product product) {
        this.product = product;
    }
    

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Date getReqDate() {
        return reqDate;
    }

    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = (status.equals("")) ? "N.A" : status;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = (priorityLevel <= 0) ? 1 : priorityLevel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = (quantity < 0) ? 0 : quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = (product == null) ? new Product() : product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = (user == null) ? new User() : user;
    }

    @Override
    public String toString() {
        return String.format("%S                        %d              %d              %S              %S              %S\n", product.getName(), quantity, priorityLevel, status, reqDate, completedDate);
    }

    //This string format is specifically used to generate reports on client requests.
    public String reportToString() {
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd/MM/yy");
        String returnString = String.format("%1s%10s%10d%10s%15s%30s%20s",
                user.getUsername(),
                product.getName(),
                quantity,
                simpleDateFormatter.format(reqDate),
                priorityLevel,
                status,
                simpleDateFormatter.format(completedDate));
        return returnString;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.user);
        hash = 97 * hash + Objects.hashCode(this.product);
        hash = 97 * hash + this.quantity;
        hash = 97 * hash + this.priorityLevel;
        hash = 97 * hash + Objects.hashCode(this.status);
        hash = 97 * hash + Objects.hashCode(this.reqDate);
        hash = 97 * hash + Objects.hashCode(this.completedDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserRequest other = (UserRequest) obj;
        if (this.quantity != other.quantity) {
            return false;
        }
        if (this.priorityLevel != other.priorityLevel) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.reqDate, other.reqDate)) {
            return false;
        }
        if (!Objects.equals(this.completedDate, other.completedDate)) {
            return false;
        }
        return true;
    }

    //Selects all user requests.
    @Override
    public ArrayList<UserRequest> select() {
        ArrayList<UserRequest> uReq = new ArrayList<UserRequest>();
        Datahandler dh = Datahandler.dataInstance;
        ResultSet rs;
        try {
            rs = dh.selectQuerySpec(Datahelper.selectRequest);
            while (rs.next()) {
                uReq.add(new UserRequest(new User(new Person(), rs.getString("Username"), rs.getString("Password"), rs.getString("AccessLevel"), rs.getString("Status")), new Product(rs.getString("Name"), rs.getString("Description"), new Category(), rs.getString("Status"),
                        new Model(), rs.getDouble("CostPrice"), rs.getDouble("SalesPrice"), rs.getDate("EntryDate")), rs.getInt("Quantity"), rs.getInt("Priority"), rs.getString("ReqStatus"), rs.getDate("ReqDate"), rs.getDate("DateCompleted")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uReq;
    }
    
    //Selects user requests that are unprocessed.
    public ArrayList<UserRequest> selectUnprocessed() {
        ArrayList<UserRequest> uReq = new ArrayList<UserRequest>();
        Datahandler dh = Datahandler.dataInstance;
        ResultSet rs;
        try {
            rs = dh.selectQuerySpec(Datahelper.selectUnprocessedRequests);
            while (rs.next()) {
                uReq.add(new UserRequest(new User(new Person(), rs.getString("Username"), rs.getString("Password"), rs.getString("AccessLevel"), rs.getString("Status")), new Product(rs.getString("Name"), rs.getString("Description"), new Category(), rs.getString("Status"),
                        new Model(), rs.getDouble("CostPrice"), rs.getDouble("SalesPrice"), rs.getDate("EntryDate")), rs.getInt("Quantity"), rs.getInt("Priority"), rs.getString("ReqStatus"), rs.getDate("ReqDate"), rs.getDate("DateCompleted")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uReq;
    }

    //Selects user requests that are unprocessed or backordered.
    public ArrayList<UserRequest> selectUnprocessed_ProductBackOrder() {
        ArrayList<UserRequest> uReq = new ArrayList<UserRequest>();
        Datahandler dh = Datahandler.dataInstance;
        ResultSet rs;
        try {
            rs = dh.selectQuerySpec(Datahelper.selectUnprocessed_BackOrderRequests);
            while (rs.next()) {
                uReq.add(new UserRequest(new User(new Person(), rs.getString("Username"), rs.getString("Password"), rs.getString("AccessLevel"), rs.getString("Status")), new Product(rs.getString("Name"), rs.getString("Description"), new Category(), rs.getString("Status"),
                        new Model(), rs.getDouble("CostPrice"), rs.getDouble("SalesPrice"), rs.getDate("EntryDate")), rs.getInt("Quantity"), rs.getInt("Priority"), rs.getString("ReqStatus"), rs.getDate("ReqDate"), rs.getDate("DateCompleted")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uReq;
    }

    //Selects all requests that have been approved, but is awaiting purchase of new stock.
    public ArrayList<UserRequest> selectRequestsAwaitingPurchase() {
        ArrayList<UserRequest> uReq = new ArrayList<UserRequest>();
        Datahandler dh = Datahandler.dataInstance;
        ResultSet rs;
        try {
            rs = dh.selectQuerySpec(Datahelper.selectRequestsAwaitingPurchase);
            while (rs.next()) {
                uReq.add(new UserRequest(new User(new Person(), rs.getString("Username"), rs.getString("Password"), rs.getString("AccessLevel"), rs.getString("Status")), new Product(rs.getString("Name"), rs.getString("Description"), new Category(), rs.getString("Status"),
                        new Model(), rs.getDouble("CostPrice"), rs.getDouble("SalesPrice"), rs.getDate("EntryDate")), rs.getInt("Quantity"), rs.getInt("Priority"), rs.getString("ReqStatus"), rs.getDate("ReqDate"), rs.getDate("DateCompleted")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uReq;
    }

    //Selects requests of a specific product that is either unprocessed or backordered.
    public ArrayList<UserRequest> selectUnprocessed_Product_BackOrder() {
        ArrayList<UserRequest> uReq = new ArrayList<UserRequest>();
        Datahandler dh = Datahandler.dataInstance;
        ResultSet rs;
        try {
            rs = dh.selectQuerySpec(Datahelper.selectUnprocessed_BackOrderRequests);
            while (rs.next()) {
                if (rs.getString("Name").equals(this.product.name)) {
                    uReq.add(new UserRequest(new User(new Person(), rs.getString("Username"), rs.getString("Password"), rs.getString("AccessLevel"), rs.getString("Status")), new Product(rs.getString("Name"), rs.getString("Description"), new Category(), rs.getString("Status"),
                            new Model(), rs.getDouble("CostPrice"), rs.getDouble("SalesPrice"), rs.getDate("EntryDate")), rs.getInt("Quantity"), rs.getInt("Priority"), rs.getString("ReqStatus"), rs.getDate("ReqDate"), rs.getDate("DateCompleted")));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uReq;
    }

    //Selects the requests of a specific user.
    public ArrayList<UserRequest> selectSpecUserRequest() {
        ArrayList<UserRequest> uReq = new ArrayList<UserRequest>();
        Datahandler dh = Datahandler.dataInstance;
        ResultSet rs;
        try {
            rs = dh.selectQuerySpec(Datahelper.selectSpecUserRequest(this.user.getUsername()));
            while (rs.next()) {
                uReq.add(new UserRequest(new User(new Person(), rs.getString("Username"), rs.getString("Password"), rs.getString("AccessLevel"), rs.getString("Status")), new Product(rs.getString("Name"), rs.getString("Description"), new Category(), rs.getString("Status"),
                        new Model(), rs.getDouble("CostPrice"), rs.getDouble("SalesPrice"), rs.getDate("EntryDate")), rs.getInt("Quantity"), rs.getInt("Priority"), rs.getString("ReqStatus"), rs.getDate("ReqDate"), rs.getDate("DateCompleted")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uReq;
    }

    //Selects all products for which there are pending requests.
    public ArrayList<Product> productsOnRequest() {
        ArrayList<Product> products = new ArrayList<Product>();
        ArrayList<UserRequest> ur = this.selectUnprocessed_ProductBackOrder();
        for (UserRequest u : ur) {
            Product p = u.getProduct().selectSpecProduct();
            products.add(p);
        }
        return products;
    }
    
    public ArrayList<User> selectUsersWithReq() {
        ArrayList<User> uReq = new ArrayList<User>();
        Datahandler dh = Datahandler.dataInstance;
        ResultSet rs;
        try {
            rs = dh.selectQuerySpec(Datahelper.selectUsersWithReq);
            while (rs.next()) {
                uReq.add(new User(new Person(rs.getString("Name"), rs.getString("Surname"), rs.getString("IDNumber"),new Address(),new Contact(rs.getString("Cell"),rs.getString("Email")),new Department(),rs.getString("Campus")),rs.getString("Username"),rs.getString("Password"),rs.getString("AccessLevel"),rs.getString("Status")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uReq;
    }

    @Override
    public synchronized int update() {
        String[][] reqVals = new String[][]{{"INT", "Quantity", Integer.toString(this.quantity)}, {"INT", "Priority", Integer.toString(this.priorityLevel)}, {"STRING", "ReqStatus", this.status}, {"DATE", "DateCompleted", this.completedDate.toString()}};
        Datahandler dh = Datahandler.dataInstance;
        try {
            return dh.performUpdate(TableSpecifiers.REQUEST.getTable(), reqVals, "`UserIDFK` = (SELECT `UserIDPK` FROM `tbluser` WHERE `Username` = '" + this.user.getUsername() + "') "
                    + "AND `ProductIDFK` = (SELECT `ProductIDPK` FROM `tblproduct` WHERE `Name` = '" + this.product.getName() + "')");
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    //Selects only unprocessed requests
    public synchronized int updateUnprocessed() {
        String[][] reqVals = new String[][]{{"INT", "Quantity", Integer.toString(this.quantity)}, {"INT", "Priority", Integer.toString(this.priorityLevel)}, {"STRING", "ReqStatus", this.status}, {"DATE", "DateCompleted", this.completedDate.toString()}};
        Datahandler dh = Datahandler.dataInstance;
        try {
            return dh.performUpdate(TableSpecifiers.REQUEST.getTable(), reqVals, "`UserIDFK` = (SELECT `UserIDPK` FROM `tbluser` WHERE `Username` = '" + this.user.getUsername() + "') "
                    + "AND `ProductIDFK` = (SELECT `ProductIDPK` FROM `tblproduct` WHERE `Name` = '" + this.product.getName() + "') AND `ReqStatus` = 'Unprocessed'");
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    //Selects only requests that are awaiting purchase
    public synchronized int updateAwaitingPurchase() {
        String[][] reqVals = new String[][]{{"INT", "Quantity", Integer.toString(this.quantity)}, {"INT", "Priority", Integer.toString(this.priorityLevel)}, {"STRING", "ReqStatus", this.status}, {"DATE", "DateCompleted", this.completedDate.toString()}};
        Datahandler dh = Datahandler.dataInstance;
        try {
            return dh.performUpdate(TableSpecifiers.REQUEST.getTable(), reqVals, "`UserIDFK` = (SELECT `UserIDPK` FROM `tbluser` WHERE `Username` = '" + this.user.getUsername() + "') "
                    + "AND `ProductIDFK` = (SELECT `ProductIDPK` FROM `tblproduct` WHERE `Name` = '" + this.product.getName() + "') AND `ReqStatus` = 'Awaiting Purchase'");
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public synchronized int delete() {
        Datahandler dh = Datahandler.dataInstance;
        try {
            return dh.performDelete(TableSpecifiers.REQUEST.getTable(), "`UserIDFK` = (SELECT `UserIDPK` FROM `tbluser` WHERE `Username` = '" + this.user.getUsername() + "') "
                    + "AND `ProductIDFK` = (SELECT `ProductIDPK` FROM `tblproduct` WHERE `Name` = '" + this.product.getName() + "')");
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public synchronized int insert() {
        String[][] reqVals = new String[][]{{"INT", "UserIDFK", "(SELECT `UserIDPK` FROM `tbluser` WHERE `Username` = '" + this.user.getUsername() + "')"},
        {"INT", "ProductIDFK", "(SELECT `ProductIDPK` FROM `tblproduct` WHERE `Name` = '" + this.product.getName() + "')"},
        {"INT", "Quantity", Integer.toString(this.quantity)}, {"INT", "Priority", Integer.toString(this.priorityLevel)}, {"STRING", "ReqStatus", this.status},
        {"DATE", "ReqDate", this.reqDate.toString()}, {"DATE", "DateCompleted", this.completedDate.toString()}};
        Datahandler dh = Datahandler.dataInstance;
        try {
            return dh.performInsert(TableSpecifiers.REQUEST.getTable(), reqVals);
        } catch (SQLException ex) {
            Logger.getLogger(UserRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

}
