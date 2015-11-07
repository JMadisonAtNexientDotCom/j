package test.dbDataAbstractions.entities.tables;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.BaseEntity;

////////////////// LOFT ABSTRACTION DOCUMENTATION START ////////////////////////
/*
Diagram relevant enough to these tables to be included in the header file:
1: twine_table
2: loft_table
3: bale_stack
4: straw_purse
       	__________________________________________________________________
       /:::::::::::::THE LOFT: (AHK Shortcut===[?lofttable]:::::::::::::::\
      /                                                                    \
     /  +--loft_table records:--------------------------------------------+ ]
    /   |           [0]                   [1]                   [2]       | ]
   /    \--> __[ bale_si ]__ ===== __[ bale_si ]__ ===== __[ bale_si ]__<-/ ]
  /         /               \     /               \     /               \   ]
 [ --+------->+----bale---+  \   /  +----bale---+  \   /  +----bale---+  \  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  +-----------+  |   |  +-----------+  |   |  +-----------+  |  ]
 [   |     |                 |   |                 |   |                 |  ]
 [ bale_   |  +----bale---+  |   |  +----bale---+  |   |  +----bale---+  |  ]
 [ stack   |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [ records |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  +-----------+  |   |  +-----------+  |   |  +-----------+  |  ]
 [   |     |                 |   |                 |   |                 |  ]
 [   |     |  +----bale---+  |   |  +----bale---+  |   |  +----bale---+  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [ --+------->+-----------+  /   \  +-----------+  /   \  +-----------+  /  ]
 [          \_______________/     \_______________/     \_______________/   ]
 +==========================================================================+
             +----+----------+----------+  KEY:============================||
twine_table: | id | loft_id  | token_id |  fk    : short for "foreign key" ||
             +----+--|-------+----------+  col   : short for "column"      ||
                     |                     wno: short for "with name of"   ||
                  +--V-+----------+        ?_id : fk to col wno "id"       ||
 loft_table:      | id |  bale_si |        ?_si : fk to col wno "stack_id" ||
                  +----+----|-----+        ?_gi : fk to col wno "group_id" ||
                            |              ================================||
              +----------+--V-+-------+----------+
 bale_stack:  | stack_id | id | locus | straw_gi |
              +-----|----+----+-------+-----|----+
                    |                       |
                    |             +----+---/|\----+-------------------+
straw_purse:        |             | id | group_id | foreign_record_id |
                    |             +----+--|-------+-----------------|-+
                    |                     |                         |
                    | stack_table:        | group_table:            |
group_table:     +--V-+----------+     +--V-+----------+            |
    AND          | id | checksum |     | id | checksum |            |
stack_table:     +----+----------+     +----+----------+            |
                                                                    |
                                            +-----------+--------+--V-+
cuecard_table                               | riddle_id | ink_id | id |
                                            +-----------+--------+----+

The straw bail is my data structure for storing an array of groups.
Each array of groups is known as a "loft". Because straw is stored
in a loft.

NOTE: XXXX_stack table is only required to have "stack_id" and "locus_id"
columns. The foreign_record_id only exists because this stack happens to be
stacking GROUPS/BUNDLES of cuecards or other information fitting the same
structure.

CAUTION: If you find ids are not working, or use of entities is crashing:
Remove these lines from EntityColumnDebugUtil.java .checkFiledType(...)
1: if(type == long.class){return;}
2: if(type == boolean.class){return;}
Pretty sure they will work. But I've had problems in past.
I'd prefer to work with such values if possible.
*/
////////////////// LOFT ABSTRACTION DOCUMENTATION END   ////////////////////////

/**
 * Twine table is used to "CAST" a collection of entries from the
 * loft_table so that we know what the foreign_record_id values referenced
 * in the straw_purse refer to.
 * 
 * Entries found in twine_table are specifically for binding cuecards
 * together into ordered groups. I was thinking to call it rubberband_table.
 * But I wanted to keep with  midevil theme. And rubberbands did not exist
 * back in the times of ninjas & jesters. But twine did.
 * So imagine the twine as something that bundles a sub-set of cards within
 * the deck.
 * 
 * @author jmadison:2015.11.02_0605PM (November,2nd,Year2015.6:05PM)
 */
@Entity
@Table(name =TwineTable.TABLE_NAME)
public class TwineTable extends BaseEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.TWINE_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    
    //Values referencing columns used for making criteria searches:
    public static final String LOFT_ID_COLUMN  = VarNameReg.LOFT_ID;
    public static final String TOKEN_ID_COLUMN = VarNameReg.TOKEN_ID;
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make not null 
    @Column(name = LOFT_ID_COLUMN)  //when getting it from database.
    public long loft_id; //<--change to value-type.
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make not null 
    @Column(name = TOKEN_ID_COLUMN)  //when getting it from database.
    public long token_id; //<--change to value-type.
    
}//CLASS::END
