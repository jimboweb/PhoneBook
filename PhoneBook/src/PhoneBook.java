import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;


public class PhoneBook {

    private FastScanner in;
    private PrintWriter out;
    // store all strings in one list
    private static String[] entries = new String[10000000];
    private static Contact[] contactArray;
    // for hash function
    private static boolean naive = false;
    private static boolean debug = false;
    private static boolean unitTest = true;
    private List<Contact> contacts = new ArrayList<>();
    private static Random rnd = new Random();
    public static void main(String[] args) {
        //new PhoneBook().processQueries();
    	new PhoneBook().unitTest();
    }

    
    private void unitTest(){
    	contactArray = new Contact[9999999];
    	int trials = 100000;
    	for(int i=0; i<trials; i++){
    		in = new FastScanner();
        	Query[] utQueries;
           	String[] naiveAnswers;
           	String[] myAnswers;	
           	List<Integer> usedIds;
			String nextOp;
			int index;
			String name;
			int nameLength;
			char nextNameChar;
    		int n = rnd.nextInt(99999);
    		utQueries = new Query[n];
    		naiveAnswers = new String[n];
    		myAnswers = new String[n];
    		usedIds = new ArrayList<Integer>();
    		System.out.printf("trying with %d queries%n", n);
    		for(int j=0;j<n;j++){
    			byte nextOpNum = (byte)rnd.nextInt(3);
    			switch(nextOpNum){
    			case 0:
    				nextOp = "find";
    				int found = rnd.nextInt(3);
					if(found<2 && usedIds.size()>1)
						index = usedIds.get(rnd.nextInt(usedIds.size()));
					else
						index = rnd.nextInt(9999999);
    				utQueries[j] = new Query(nextOp, index);
    				break;
    			case 1:
    				nextOp = "del";
    				found = rnd.nextInt(3);
					if(found<1 && usedIds.size()>1)
						index = usedIds.get(rnd.nextInt(usedIds.size()));
					else
						index = rnd.nextInt(9999999);
    				utQueries[j] = new Query(nextOp, index);
    				break;
    			default:
    				nextOp = "add";
    				index = rnd.nextInt(9999999);
    				utQueries[j] = new Query(nextOp, index);
    				nameLength = rnd.nextInt(14) + 1;
    				name = "";
    				for(i=0; i<nameLength; i++){
    					int charNum = rnd.nextInt(26) + (int)'a';
    					nextNameChar = (char)(charNum);
    					name += nextNameChar;
    				}
    				utQueries[j] = new Query(nextOp, name, index);
    				usedIds.add(index);
    				break;
    			}
    			naiveAnswers[j] = processQueryNaive(utQueries[j]);
    			myAnswers[j] = processQuery(utQueries[j]);
    			String myAnswer = myAnswers[j]==null?"null":myAnswers[j];
    			String naiveAnswer = naiveAnswers[j]==null?"null":naiveAnswers[j];
    			if(!naiveAnswer.equals(myAnswer)){
    					System.out.printf("Incorrect answer on trial %d query %d%n", i, j);
    					if(utQueries[j].name == null)
    						System.out.printf("Query was type = %s index = %d%n", utQueries[j].type, utQueries[j].number);
    					else 
    						System.out.printf("Query was type = %s index = %d name = %s%n", utQueries[j].type, utQueries[j].number, utQueries[j].name);
    					System.out.printf("Correct output was %s, your output was %s%n%n",naiveAnswer, myAnswer);
    					in.next();
    				
    			}
    		}
    	}
    }
    
    

    private String processQuery(Query query) {
        if (query.type.equals("add")) {
        	entries[query.number] = query.name;
        } else if (query.type.equals("del")) {
        	entries[query.number] = null;
        
        } else {
        	if(entries[query.number]== null){
        		String result = "not found";
        		if(unitTest)
        			return result;
        	} else {
        		return entries[query.number];
        	}
        }
        return null;
    }
   
    
    
    
    
    
    
    
    
    
    
    
    


    private Query readQuery() {
        String type = in.next();
        int number = in.nextInt();
        if (type.equals("add")) {
            String name = in.next();
            return new Query(type, name, number);
        } else {
            return new Query(type, number);
        }
    }

    private String processQuery2(Query query) {
        if (query.type.equals("add")) {
            // if we already have contact with such number,
            // we should rewrite contact's name
            // otherwise, just add it
            contactArray[query.number] = new Contact(query.name, query.number);
        } else if (query.type.equals("del")) {
            contactArray[query.number] = null;
        } else {
            String response = "not found";
            if(contactArray[query.number]!=null)
            	response = contactArray[query.number].name;
            return response;
        }
        return null;
    }

    private String processQueryNaive(Query query) {
        if (query.type.equals("add")) {
            // if we already have contact with such number,
            // we should rewrite contact's name
            boolean wasFound = false;
            for (Contact contact : contacts)
                if (contact.number == query.number) {
                    contact.name = query.name;
                    wasFound = true;
                    break;
                }
            // otherwise, just add it
            if (!wasFound)
                contacts.add(new Contact(query.name, query.number));
        } else if (query.type.equals("del")) {
            for (Iterator<Contact> it = contacts.iterator(); it.hasNext(); )
                if (it.next().number == query.number) {
                    it.remove();
                    break;
                }
        } else {
            String response = "not found";
            for (Contact contact: contacts)
                if (contact.number == query.number) {
                    response = contact.name;
                    break;
                }
            return response;
        }
        return null;
    }

    public void processQueries() {
    	contactArray = new Contact[10000000];
    	in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        int queryCount = in.nextInt();
        String result;
        for (int i = 0; i < queryCount; ++i){
        	if(naive){
        		result = processQueryNaive(readQuery());
        		if(result != null){
        			System.out.println(result);        			
        		}
        	}
        	else{
        		result = processQuery2(readQuery());
        		if(result!=null){
        			System.out.println(result);
        		}
        			
        	}
        }
        
    }

    static class Contact {
        String name;
        int number;

        public Contact(String name, int number) {
            this.name = name;
            this.number = number;
        }
    }

    static class Query {
        String type;
        String name;
        int number;

        public Query(String type, String name, int number) {
            this.type = type;
            this.name = name;
            this.number = number;
        }

        public Query(String type, int number) {
            this.type = type;
            this.number = number;
        }
    }

    class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}

