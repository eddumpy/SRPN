import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Stack;

public class SRPN {

	Stack<String> srpnStack = new Stack<>(); // creates a stack
	int n = 0; // this int is used for randomNumber

	public boolean overflow() {

		// returns boolean if stack is overflowing and an error message if it is

		if (srpnStack.size() > 22) {
			System.out.println("Stack overflow.");
			return true;
		}

		else {
			return false;
		}
	}

	public String bigInt(String b) {

		// all strings that match with just numbers will be checked to see if they are
		// too big
		// if they are max or min int value is returned

		String max = Integer.toString(Integer.MAX_VALUE);
		String min = Integer.toString(Integer.MIN_VALUE);

		BigInteger bg = new BigInteger(b);
		BigInteger maxInt = new BigInteger(max);
		BigInteger minInt = new BigInteger(min);

		if (bg.compareTo(maxInt) == 1) {
			return max;
		}

		else if (bg.compareTo(minInt) == -1) {
			return min;
		}

		return b;

	}

	public String randomNumber() {

		String[] rArray = { "1804289383", "846930886", "1681692777", "1714636915", "1957747793", "424238335",
				"719885386", "1649760492", "596516649", "1189641421", "1025202362", "1350490027", "783368690",
				"1102520059", "2044897763", "1967513926", "1365180540", "1540383426", "304089172", "1303455736",
				"35005211", "521595368", "294702567", "1726956429", "336465782", "861021530", "278722862", "233665123",
				"2145174067", "468703135", "1101513929", "1801979802", "1315634022", "635723058", "1369133069",
				"1125898167", "1059961393", "2089018456", "628175011", "1656478042", "1131176229", "1653377373",
				"859484421", "1914544919", "608413784", "756898537", "1734575198", "1973594324", "149798315",
				"2038664370", "1129566413", "184803526", "412776091", "1424268980", "1911759956", "749241873",
				"137806862", "42999170", "982906996", "135497281" };

		if (n > 59) {
			n = 0;
			return rArray[n];
		}

		else {
			return rArray[n];
		}
	}

	public void processOctal(String octal) {

		// finds the decimal value of the octal number received from input

		long o = Long.parseLong(octal);
		long decimal = 0;
		int i = 0;

		/*
		 * https://www.programiz.com/java-programming/examples/octal-decimal-convert
		 * this section of code was based off of this link
		 */

		while (o != 0) {

			decimal += (o % 10) * Math.pow(8, i);
			i++;
			o /= 10;
		}

		String octalFinal = Long.toString(decimal);

		if (overflow() == false) {
			srpnStack.push(octalFinal);
		}
	}

	public void processException(String e) {

		// if string is "=" it points to top of the stack, or if empty it produces an
		// error message.
		if (e.equals("=")) {
			if (srpnStack.isEmpty()) {
				System.out.println("Stack Empty.");
			}

			else {
				System.out.println(srpnStack.peek());
			}
		}

		// if string is "r" it will print the same random numbers each time
		else if (e.equals("r")) {
			if (overflow() == false) {
				srpnStack.push(randomNumber());
				n++; // adds n so, the next number in the array is printed when r is next entered
			}
		}

		// if "d" is found in string, it will print out the top of the stack
		// this is done through printing each number of an array on a new line.
		else if (e.equals("d")) {
			Object[] srpnArray = srpnStack.toArray();
			for (int i = 0; i < srpnArray.length; i++) {
				System.out.println(srpnArray[i]);
			}
		}

		// this catches any strings not recognised and prints the character
		// as an error message
		else {
			System.out.println("Unrecognised operator or operand \"" + e + "\".");
		}
	}

	public void processOperator(String o) {

		// before operation is performed it checks if stack is big enough first,
		// if so it gets processed if not error message is printed

		if (srpnStack.size() > 1) {

			// removes the last two numbers put on stack and parses it into a type long
			String a = srpnStack.pop();
			String b = srpnStack.pop();
			long num1 = Long.parseLong(a);
			long num2 = Long.parseLong(b);

			// string to perform result of operation and check saturation
			String result, sat;

			switch (o) {

			// each operator finds the result of the operation, parses it to a string
			// checks for saturation and size of stack and then is pushed to the stack

			case "+":
				result = Long.toString(num2 + num1);
				sat = bigInt(result);
				srpnStack.push(sat);
				break;

			case "-":
				result = Long.toString(num2 - num1);
				sat = bigInt(result);
				srpnStack.push(sat);
				break;

			case "*":
				result = Long.toString(num2 * num1);
				sat = bigInt(result);
				srpnStack.push(sat);
				break;

			case "/":
				// if num1 is 0 operation can't be performed and numbers get returned to stack
				if (num1 == 0 | num2 == 0) {
					System.out.println("Divide by 0.");
					a = String.valueOf(num1);
					b = String.valueOf(num2);
					srpnStack.push(b);
					srpnStack.push(a);
				} else {
					result = Long.toString(num2 / num1);
					sat = bigInt(result);
					srpnStack.push(sat);
				}
				break;

			case "^":
				// if num1 is not negative then it performs the operation normally otherwise
				// error message is produced
				if (num1 > 0) {
					int i = (int) Math.pow(num2, num1);
					result = String.valueOf(i);
					sat = bigInt(result);
					srpnStack.push(sat);
				}

				else {
					System.out.println("Negative power.");
					a = String.valueOf(num1);
					b = String.valueOf(num2);
					srpnStack.push(b);
					srpnStack.push(a);
				}
				break;

			case "%":
				result = Long.toString(num2 % num1);
				sat = bigInt(result);
				srpnStack.push(sat);
				break;
			}
		}

		// if stack is not big enough, it will leave it alone and print an error
		// message.
		else {
			System.out.println("Stack underflow.");
		}
	}

	public void processSubStrings(String s) {

		String bg; // string is used for checking saturation later in the method
		// below is a regular expression to split string by operators and numbers, and
		// any character not recognised by srpn
		String[] tokenArray = s.split("(?<=[+/=^%])|(?=[-+/=%^])|(?<=[^0-9-+/=%^])|(?=[^0-9-+/=%^])");
		for (int i = 0; i < tokenArray.length; i++) {

			// finds strings with positive numbers

			if (tokenArray[i].matches("^[0-9]+")) {

				/*
				 * any values caught in this if statement will be checked to see if they are too
				 * big. Values will also check if the input is an octal number, this is
				 * represented by starting with a zero, and splits the string when it meets an 8
				 * or a 9 otherwise it is pushed to stack
				 */

				bg = bigInt(tokenArray[i]);

				if (bg.startsWith("0") & !bg.contains("8") & !bg.contains("9")) {
					processOctal(bg);
				}

				else if (bg.startsWith("0") & (bg.contains("8") | bg.contains("9"))) {
					String[] octalArray = bg.split("(?=[89])");
					processOctal(octalArray[0]);
				}

				else {
					if (overflow() == false) {
						srpnStack.push(bg);
					}
				}
			}

			// this works the same as the previous if statement but for minus numbers

			else if (tokenArray[i].matches("-?[0-9]+")) {

				bg = bigInt(tokenArray[i]);
				if (bg.contains("-0") & !bg.contains("8") & !bg.contains("9")) {
					processOctal(bg);
				}

				else if (bg.contains("-0") & (bg.contains("8") | bg.contains("9"))) {
					String[] octalArray = bg.split("(?=[89])");
					processOctal(octalArray[0]);
				}

				else {
					if (overflow() == false) {
						srpnStack.push(bg);
					}
				}
			}

			// catches any of the operators and sends them to processOperator method
			else if (tokenArray[i].equals("+") | tokenArray[i].equals("-") | tokenArray[i].equals("*")
					| tokenArray[i].equals("/") | tokenArray[i].equals("^") | tokenArray[i].equals("%")) {
				processOperator(tokenArray[i]);
			}

			// finds substrings with =, r and d, then sends them to processException method
			else if (tokenArray[i].matches("[=rd]")) {
				processException(tokenArray[i]);
			}

			// hidden function in jar file
			else if (s.equals("rachid")) {
				System.out.println("Rachid is the best unit lecturer.");
			}

			// if nothing is entered, nothing happens
			else if (s.equals("")) {
				System.out.print("");
			}

			// if the string consists of any other unrecognised characters it will be sent
			// to the exception method
			else {
				processException(tokenArray[i]);
			}
		}
	}

	public void processString(String s) {

		// if the string starts with # it is left alone
		if (s.startsWith("#")) {
			System.out.print("");
		}

		// if s contains white space it is split into an array and each
		// string in the array is sent to be processed individually
		else if (s.contains(" ")) {
			String[] tokenArray = s.split(" ");
			for (int i = 0; i < tokenArray.length; i++) {
				processSubStrings(tokenArray[i]);
			}
		}

		// else process any strings which do not start with # or contain whitespace
		else {
			processSubStrings(s);
		}
	}

	public void processCommand(String s) {

		// command is first processed to see if there is a comment in the input
		// if so it is split into two strings and the the string before the # char is
		// sent to the next class

		// splits string up if a comment is found
		if (s.contains("#")) {
			String[] commentArray = s.split("(?=[#])");
			for (int l = 0; l < commentArray.length; l++) {
				processString(commentArray[l]);
			}
		}

		// if there is no comment it is sent to be processed
		else {
			processString(s);
		}
	}

	public static void main(String[] args) {
		SRPN srpn = new SRPN();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
			// Keep on accepting input from the command-line
			while (true) {
				String command = reader.readLine();

				// Close on an End-of-file (EOF) (Ctrl-D on the terminal)
				if (command == null) {
					// Exit code 0 for a graceful exit
					System.exit(0);
				}

				// Otherwise, (attempt to) process the character
				srpn.processCommand(command);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
