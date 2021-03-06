package simpledb;

import java.util.*;

/**
 * SeqScan is an implementation of a sequential scan access method that reads
 * each tuple of a table in no particular order (e.g., as they are laid out on
 * disk).
 */
public class SeqScan implements DbIterator {

    private static final long serialVersionUID = 1L;

    /**
     * The transaction this scan is running as part of
     */
    private TransactionId tid;
    
    /**
     * the table to scan
     */
    private int tableid;
    
    /**
     * the alias of the table
     */
    private String tableAlias;
    
    /**
     * the iterator over the scanned table
     */
    private DbFileIterator dbFileItr;
    
    /**
     * the name of the table
     */
    private String tableName;
    
    /**
     * the tupleDesc of the table
     */
    private TupleDesc tupleDesc;
    
    /**
     * Creates a sequential scan over the specified table as a part of the
     * specified transaction.
     *
     * @param tid
     *            The transaction this scan is running as a part of.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    public SeqScan(TransactionId tid, int tableid, String tableAlias) {
        // some code goes here
    	this.tid = tid;
    	this.tableid = tableid;
    	this.tableAlias = tableAlias;
    	this.dbFileItr = Database.getCatalog().getDatabaseFile(tableid).iterator(tid);
    	this.tableName = Database.getCatalog().getTableName(tableid);
    	this.tupleDesc = Database.getCatalog().getTupleDesc(tableid);
    }

    /**
     * @return
     *       return the table name of the table the operator scans. This should
     *       be the actual name of the table in the catalog of the database
     * */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * @return Return the alias of the table this operator scans.
     * */
    public String getAlias() {
        // some code goes here
        return this.tableAlias;
    }

    /**
     * Reset the tableid, and tableAlias of this operator.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    public void reset(int tableid, String tableAlias) {
        this.tableid = tableid;
        this.tableAlias = tableAlias;
        this.dbFileItr = Database.getCatalog().getDatabaseFile(tableid).iterator(this.tid);
        this.tupleDesc = Database.getCatalog().getTupleDesc(tableid);
    }

    public SeqScan(TransactionId tid, int tableid) {
        this(tid, tableid, Database.getCatalog().getTableName(tableid));
    }

    public void open() throws DbException, TransactionAbortedException {
        // some code goes here
    	this.dbFileItr.open();
    }

    /**
     * Returns the TupleDesc with field names from the underlying HeapFile,
     * prefixed with the tableAlias string from the constructor. This prefix
     * becomes useful when joining tables containing a field(s) with the same
     * name.  The alias and name should be separated with a "." character
     * (e.g., "alias.fieldName").
     *
     * @return the TupleDesc with field names from the underlying HeapFile,
     *         prefixed with the tableAlias string from the constructor.
     */
    public TupleDesc getTupleDesc() {
        if (this.tableAlias.equals("")) {
        	return this.tupleDesc;
        }
        TupleDesc td = this.tupleDesc;
        Type[] typeAr = new Type[td.numFields()];
        String[] fieldAr = new String[td.numFields()];
        for (int i = 0; i < td.numFields(); i++) {
        	typeAr[i] = td.getFieldType(i);
        	fieldAr[i] = this.tableAlias + "." + td.getFieldName(i);
        }
        return new TupleDesc(typeAr, fieldAr);
    }

    public boolean hasNext() throws TransactionAbortedException, DbException {
        // some code goes here
        return this.dbFileItr.hasNext();
    }

    public Tuple next() throws NoSuchElementException,
            TransactionAbortedException, DbException {
        // some code goes here
        return this.dbFileItr.next();
    }

    public void close() {
        // some code goes here
    	this.dbFileItr.close();
    }

    public void rewind() throws DbException, NoSuchElementException,
            TransactionAbortedException {
        // some code goes here
    	this.dbFileItr.rewind();
    }
}
