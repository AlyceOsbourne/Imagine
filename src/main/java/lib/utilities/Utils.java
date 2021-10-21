/*
 * Do what the F**k you want
 */

/*
 * Do what the F**k you want
 */

package lib.utilities;

import lib.math.voronoi.algorithm.data.nodes.Point;
import org.jetbrains.annotations.Contract;

public final class Utils {
	public enum Resolution {
		HIGH(1920, 1200),
		MEDIUM(HIGH.width / 2, HIGH.height / 2),
		LOW(MEDIUM.width / 2, MEDIUM.height / 2);


		final public int width, height;

		Resolution(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

	public static class DistanceUtils {
		public static Point midpoint(Point a, Point b, Point[][] matrix) {
			return matrix[average(a.x, b.x)][average(a.y, b.y)];
		}

		@Contract(pure = true)
		private static int average(int a, int b) {
			return (a + b) / 2;
		}
	}

	public static class LoadingUtils {
		public static void progressPercentage(int done, int total) {
			int size = 100;
			String iconLeftBoundary = "[", iconDone = "=", iconRemain = ".", iconRightBoundary = "]";

			if (done > total) {
				throw new IllegalArgumentException();
			}
			int donePercents = (100 * done) / total, doneLength = size * donePercents / 100;

			StringBuilder bar = new StringBuilder(iconLeftBoundary);
			for (int i = 0; i < size; i++) {
				if (i < doneLength) {
					bar.append(iconDone);
				} else {
					bar.append(iconRemain);
				}
			}
			bar.append(iconRightBoundary);

			System.out.print("\r" + bar + " " + donePercents + "%");

			if (done == total) {
				System.out.print("\n");
			}
		}

		@Contract(pure = true)
		public static String formatTime(long millis) {
			long secs = millis / 1000;
			return String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
		}
	}

	public static class ArrayUtils {
		public static <T> String arrayDebug2D(T[][] arr) {

			int rows = arr.length;
			int columns = findLongestRow(arr);

			String[][] asStrings = new String[rows][columns];

			for (int i = 0; i < rows; ++i) {
				for (int j = 0; j < columns; ++j) {
					asStrings[i][j] = "";
					if (j < arr[i].length) {
						if (arr[i][j] == null) {
							asStrings[i][j] = "null";
						} else {
							asStrings[i][j] = arr[i][j].toString();
						}
					}
				}
			}
			return arrayDebug2D(asStrings);
		}

		private static <T> int findLongestRow(T[][] arr) {
			int retVal = 0;
			//int depth = arr.length ;
			for (T[] ts : arr) {
				int x = ts.length;
				if (x > retVal) {
					retVal = x;
				}
			}
			return retVal;
		}

		private static String arrayDebug2D(String[][] arr) {

			if (arr == null || arr.length == 0)
				return "";

			StringBuilder x = new StringBuilder();
			int size = arr.length; //"depth" of the array

			int longestSubstring = addSpace(arr);


			for (String[] strings : arr) {

				for (String string : strings) {
					x.append("[").append(string).append("] "); //print all of the first row
				}

				x = new StringBuilder(x.substring(0, x.length() - 1)); //remove extra space
				x.append("\n"); //start a new line before going to the next row
			}
			//create top header
			int rows = findLongestRow(arr);
			//rows ==4 for this example
			StringBuilder top = new StringBuilder();

			for (int i = 0; i < rows; ++i) {
				String num = Integer.toString(i);
				int sizeI = num.length(); // 1 if <10, 2if <100, etc.
				int spacesToAdd = longestSubstring - sizeI; //add length - (i length) spaces, then place I in

				top.append(" ".repeat(Math.max(0, spacesToAdd)));

				top.append(num).append("] ");
			}
			//add starting brackets
			for (int i = 0; i < rows; ++i) {
				top = new StringBuilder(stringAdd(top.toString(), (i * (longestSubstring + 3)), "[")); //+3 because of "[] " = 3
			}

			top = new StringBuilder(top.substring(0, top.length() - 1)); //remove the final space

			//only works up to 99, but whatever)
			if (size < 10) {
				x.insert(0, "[0] ");
			} else {
				x.insert(0, "[ 0] ");
			}

			if (size < 10) {
				top.insert(0, "    ");
			} else {
				top.insert(0, "   ");
			}

			for (int i = 0; i < size - 1; ++i) {
				int b = indexOfInstance(x.toString(), i);
				x = new StringBuilder(stringAdd(x.toString(), b + 1, "[" + (i + 1) + "] "));
			}
			x.insert(0, "" + top + "\n"); //append add the header

			return x.toString();
		}

		//adds spaces to the array (void)
		private static int addSpace(String[][] arr) {
			int a = findLongestString(arr);
			int height = arr.length;
			for (int i = 0; i < height; ++i) {
				int thisWidth = arr[i].length;
				for (int j = 0; j < thisWidth; ++j) {
					int thisLength = arr[i][j].length();
					for (int k = thisLength; k < a; ++k) {
						arr[i][j] = " " + arr[i][j];
					}
				}
			}
			return a; //return the longest substring
		}

		private static String stringAdd(String original, int position, String toAdd) {
			String temp = original;
			int tempSize = temp.length();
			String first = temp.substring(0, position);
			String second = temp.substring(position, tempSize);

			original = first + toAdd + second;
			return original;
		}

		//finds where an instance of a string (char) is (0 = first), (1 = second) etc
		private static int indexOfInstance(String string, int instance) {
			int size = string.length();
			char a = '\n';
			int instanceCount = -1;
			for (int i = 0; i < size; ++i) {
				if (string.charAt(i) == a) {
					++instanceCount;
					if (instanceCount == instance) {
						return i;
					}
				}
			}
			return -1;
		}

		private static int findLongestString(String[][] arr) {
			//"depth" of the array
			int longestSubstring = 0;

			for (String[] strings : arr) {
				for (String string : strings) {
					int y = string.length(); //size of the current substring
					if (y > longestSubstring) {
						longestSubstring = y;
					}
				}
			}
			return longestSubstring;
		}

	}
}
