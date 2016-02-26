
package memogem.coreapplication;
import java.util.Objects;

/**
*Connection-class holds the type of connection between two cards and
*knows which cards it relates to.
 */
public class CardConnection {
    private LogicalConnection connectionType; //type of connection
    private Card connectionToCard; //connection to which card

    public CardConnection(LogicalConnection connectionType, Card connectionToCard) {
        this.connectionType = connectionType;
        this.connectionToCard = connectionToCard;
    }
   
    public LogicalConnection getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(LogicalConnection suhdetyyppi) {
        this.connectionType = suhdetyyppi;
    }

    public Card getConnectionTocard() {
        return connectionToCard;
    }
  

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.connectionType);
        hash = 59 * hash + Objects.hashCode(this.connectionToCard);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CardConnection other = (CardConnection) obj;
        return this.connectionToCard == other.connectionToCard && this.connectionType == other.connectionType;
        
    }

}
