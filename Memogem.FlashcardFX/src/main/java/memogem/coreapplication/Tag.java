
package memogem.coreapplication;
    /** 
     * Tag-class simply holds an attribute "String name" and is saved as an attribute of the Card-class.
     * Function: can be used in order to search different topics from the database.
     */

public class Tag {
    
    private String name;
    
    public Tag(String name) {
        this.name = name;
    }
    /**
     * Returns a name of the Tag as a String
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Sets a new name
     * @param nimi new name as a String-type object
     */
    public void setName(String nimi) {
        this.name = nimi;
    }
    
    @Override
    public boolean equals(Object t) {
        if (t == null) return false;
        if (t.getClass() != this.getClass()) {
            return false;
        }
        
        Tag taagi = (Tag) t;
        if (this.name.equals(taagi.name)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    

}
