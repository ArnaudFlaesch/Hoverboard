package hoverboard;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;      
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * BDD est la classe qui instancie une connexion à la base de données et qui contient les requêtes SQL permettant d'intéragir avec celle-ci.
 * @author Arnaud
 */

public class BDD {
    
    Connection dbcon = null;
    String requete;
    Statement statement;
    ResultSet result;
    String dbUrl, driver, user, password;
    
    /**
     * Initialise la connexion à la base de données
     * @param dbUrl
     * Adresse de la base de données
     * @param driver
     * Nom du driver utilisé
     * @param user
     * Login permettant de se connecter à la base de données
     * @param password 
     * Mot de passe permettant de se connecter à la base de données
     */
    
    public BDD(String dbUrl, String driver, String user, String password) {
        try {
            this.dbUrl=dbUrl;
            Class.forName(driver);
            dbcon = DriverManager.getConnection(dbUrl,user,password);
            this.statement = dbcon.createStatement();
        }
        catch (ClassNotFoundException | SQLException conn_error) {
           System.out.println("Error while connecting to database "+conn_error); 
        }
    }
    
    public void ajouteWidget(int positionX, int positionY, int height, int width, int idDashboard) {
        this.requete = "INSERT INTO elements VALUES (NULL, '', "+positionX+", "+positionY+", "+height+", "+width+", "+idDashboard+", 1) "; // 1 veut dire qu'on ajoute un post-it, à changer plus tard
        try {
            statement.executeUpdate(this.requete);
        }
        catch (SQLException error) {
            System.out.println(error);
        }
    }
    
    /**
     * Est exécutée lorsqu'un utilisateur essaye de se connecter à l'application. Vérifie que le login et le mot de passe saisis sont valides.
     * @param login
     * Le login saisit par l'utilisateur via la fenêtre de login.
     * @param password
     * Le mot de passe saisit par l'utilisateur via la fenêtre de login.
     * @return 
     * True si il exite bien un utilisateur, False si il n'en existe aucun ou qu'une erreur est levée.
     */
    
    public boolean connect_user(String login, String password) {
        requete = ("SELECT * FROM users WHERE login ='"+login+"' AND password ='"+password+"'");
        try {
            this.statement = dbcon.createStatement();
            result = statement.executeQuery(requete);
            if (!result.isBeforeFirst()) {
                return (false);
            }
            else {
                result.next();
                String lastName = result.getString("lastName");
                String firstName = result.getString("firstName");
                return (true);
            }
        }
        catch (SQLException error) {
            System.out.println (error);
        }
        return (false);
    }
    
    public ResultSet getNewWidgets(int idDashboard) {
        HashMap dicto = new HashMap();
        this.requete = "SELECT E.*, T.nomTypePost FROM elements E, type_element T WHERE idDashboard = 3 AND E.idTypePost = T.idTypePost";
        try {
            this.statement = dbcon.createStatement();
            this.result = statement.executeQuery(requete);
            if (!this.result.isBeforeFirst()) {
                return (this.result);
            }
            else {
                // Faire un tableau de HashMap ?
                /*
                result.next();
                dicto.put("content",result.getString("contentElement"));
                dicto.put("positionX",result.getString("positionX"));
                dicto.put("positionY",result.getString("positionY"));
                dicto.put("length",result.getString("longueur"));
                dicto.put("width",result.getString("largeur"));
                */
                return (this.result);
            }
        }
        catch (SQLException error) {
            System.out.println (error);
        }
        return (this.result);
    }
    
    
    /**
     * Appellée lorsqu'un utilisateur demande la création d'un compte sur l'application. Elle vérifie qu'il n'existe pas déjà
     * un utilisateur utilisant ce login et ce mot de passe, sinon elle l'inscrit.
     * @param firstName
     * Prénom saisit par l'utilisateur.
     * @param lastName
     * Nom de famille saisit par l'utilisateur.
     * @param login
     * Login choisit par l'utilisateur
     * @param password
     * Mot de passe choisit par l'utilisateur.
     * @param email
     * Email saisit par l'utilisateur.
     * @return 
     * True si l'utilisateur a bien été inscrit, False sinon.
     */
    
    public boolean registerUser(String firstName, String lastName, String email, String login, String password) {
        requete = ("SELECT email, login FROM users WHERE email = '"+email+"' OR login = '"+login+"'");
        try {
            this.statement = dbcon.createStatement();
            result = statement.executeQuery(requete);
            if (!result.isBeforeFirst()) {
                // Pas d'users existant, on peut inscrire l'user
                requete = "INSERT INTO users VALUES (NULL, '"+firstName+"', '"+lastName+"', '"+email+"', '"+login+"', '"+password+"' ,0 ,0) ";
                statement.executeUpdate(requete);
            }
            else {
                return (false);
            }
        }
        catch (SQLException error) {
             System.out.println(error);   
        }
        return (true);
    }
    
    /**
     * Cherche dans la base de données si un utilisateur existe avec cet email et change le mot de passe.
     * @param emailUser
     * L'adresse email renseignée dans le formulaire.
     * @return 
     * True si toutes les actions ont été effectuées, False si l'utilisateur n'existe pas ou que la connexion à la base de données n'a pas fonctionnée.
     */
    
    public boolean resetPassword(String emailUser) {
        requete = "SELECT * FROM users WHERE email ='"+emailUser+"'";
        try {
            this.statement = dbcon.createStatement();
            result = statement.executeQuery(requete);
            if (!result.isBeforeFirst()) {
                // Aucun utilisateur avec cet email
                return (false);
            }
            else {
                // On doit réinitialiser le mot de passe de l'utilisateur ici
                // Une idée : Update From users; Set password = random(passsword)
                // On envoie le nouveau password par mail à l'utilisateur
                return (true);
            }
        }
        catch (SQLException error) {
            System.out.println (error);
        }
        return (false);
    }
}