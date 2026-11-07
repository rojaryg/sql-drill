package com.example.data

import com.example.model.Difficulty
import com.example.model.DifficultyFilter
import com.example.model.Question

object SqlQuestionRepository {

    private val questions: List<Question> = listOf(
        // ================= BASIC =================
        Question(
            id = "b1",
            title = "What will this query return?",
            topic = "SELECT & WHERE",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                SELECT first_name, department
                FROM employees
                WHERE salary >= 60000 
                  AND department = 'Engineering';
            """.trimIndent(),
            options = listOf(
                "All employees in Engineering, regardless of salary",
                "Employees in Engineering who earn at least 60,000",
                "Employees earning over 60,000 across all departments",
                "The highest paid engineer's salary"
            ),
            correctOptionIndex = 1,
            explanation = "The AND operator ensures both conditions must be true: the salary must be 60,000 or greater, AND the department must match 'Engineering'."
        ),
        Question(
            id = "b2",
            title = "Which statement best describes the output order?",
            topic = "Sorting & Limiting",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                SELECT product_name, price
                FROM products
                ORDER BY price DESC, product_name ASC
                LIMIT 5;
            """.trimIndent(),
            options = listOf(
                "5 cheapest products ordered alphabetically",
                "5 most expensive products; ties sorted alphabetically",
                "5 random products sorted by price ascending",
                "All products sorted descending without any limit"
            ),
            correctOptionIndex = 1,
            explanation = "ORDER BY price DESC sorts the highest prices first. When two products share the same price, product_name ASC breaks the tie alphabetically. LIMIT 5 restricts the output to the top 5 rows."
        ),
        Question(
            id = "b3",
            title = "What does the LIKE operator with '%a_' match?",
            topic = "Pattern Matching",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                SELECT username
                FROM users
                WHERE username LIKE '%a_';
            """.trimIndent(),
            options = listOf(
                "Usernames that begin with the letter 'a'",
                "Usernames containing an 'a' as the second-to-last character",
                "Usernames with exactly two characters",
                "Usernames ending with the letter 'a'"
            ),
            correctOptionIndex = 1,
            explanation = "In SQL LIKE patterns, '%' represents zero or more characters, and '_' represents exactly one single character. Therefore, '%a_' matches any string that has 'a' followed by exactly one character at the end (second-to-last character)."
        ),
        Question(
            id = "b4",
            title = "Why does this query return zero rows when NULLs exist?",
            topic = "NULL Handling",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                -- Trying to find customers without a phone number
                SELECT customer_id, name
                FROM customers
                WHERE phone = NULL;
            """.trimIndent(),
            options = listOf(
                "The column name must be wrapped in backticks",
                "In SQL, comparisons with NULL using '=' always evaluate to UNKNOWN/false; 'IS NULL' must be used",
                "phone is a reserved keyword in SQL",
                "NULL can only be used with the HAVING clause"
            ),
            correctOptionIndex = 1,
            explanation = "NULL represents an unknown value. In three-valued SQL logic, 'expression = NULL' always evaluates to UNKNOWN (which filters out the row). You must write 'phone IS NULL' to properly test for missing values."
        ),
        Question(
            id = "b5",
            title = "What does DISTINCT operate on in this statement?",
            topic = "DISTINCT Keyword",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                SELECT DISTINCT city, state
                FROM stores;
            """.trimIndent(),
            options = listOf(
                "Only the city column; state can still duplicate",
                "Only the state column",
                "Unique combinations of both city AND state together",
                "The first 100 non-null values"
            ),
            correctOptionIndex = 2,
            explanation = "SELECT DISTINCT applies to the entire set of selected columns that follow it, returning unique tuples of (city, state). For example, ('Springfield', 'IL') and ('Springfield', 'MO') are distinct and both returned."
        ),
        Question(
            id = "b6",
            title = "Is the boundary value 100 included in this result?",
            topic = "BETWEEN Operator",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                SELECT order_id, total_amount
                FROM orders
                WHERE total_amount BETWEEN 50 AND 100;
            """.trimIndent(),
            options = listOf(
                "No, BETWEEN is always strictly exclusive (50 < x < 100)",
                "Yes, BETWEEN in SQL is inclusive of both endpoints (50 <= x <= 100)",
                "Only 50 is included, 100 is excluded",
                "Only 100 is included, 50 is excluded"
            ),
            correctOptionIndex = 1,
            explanation = "In standard SQL, the BETWEEN operator is inclusive, equivalent to 'total_amount >= 50 AND total_amount <= 100'. Orders with total amounts exactly equal to 50 or 100 are included."
        ),
        Question(
            id = "b7",
            title = "What is the result of using COUNT(column) vs COUNT(*)?",
            topic = "Aggregate Functions",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                -- Table has 10 rows: 8 with emails, 2 with NULL emails
                SELECT COUNT(email), COUNT(*)
                FROM members;
            """.trimIndent(),
            options = listOf(
                "10 and 10",
                "8 and 10",
                "8 and 8",
                "0 and 10"
            ),
            correctOptionIndex = 1,
            explanation = "COUNT(column_name) ignores NULL values and counts only rows where the specified column is not NULL (8). In contrast, COUNT(*) counts all rows regardless of NULL values (10)."
        ),
        Question(
            id = "b8",
            title = "What will this IN operator filter?",
            topic = "IN Operator",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                SELECT title, status
                FROM tasks
                WHERE status IN ('Pending', 'In Progress');
            """.trimIndent(),
            options = listOf(
                "Tasks where status is neither Pending nor In Progress",
                "Tasks where status equals either 'Pending' OR 'In Progress'",
                "Tasks where status equals both simultaneously",
                "Only tasks where status starts with 'P'"
            ),
            correctOptionIndex = 1,
            explanation = "The IN operator checks if a value matches any value in a list of literals, functioning as a shorthand for 'status = 'Pending' OR status = 'In Progress''."
        ),
        Question(
            id = "b9",
            title = "What does OFFSET 10 LIMIT 5 achieve?",
            topic = "Pagination (LIMIT & OFFSET)",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                SELECT user_id, username
                FROM users
                ORDER BY user_id
                LIMIT 5 OFFSET 10;
            """.trimIndent(),
            options = listOf(
                "Skips the first 10 rows and returns the next 5 rows",
                "Returns the first 10 rows then stops after 5 seconds",
                "Returns rows between index 5 and 10",
                "Fetches 5 random rows repeated 10 times"
            ),
            correctOptionIndex = 0,
            explanation = "OFFSET skips the designated number of rows before beginning to return rows from the query, and LIMIT caps the maximum number of rows returned. This is standard pagination."
        ),
        Question(
            id = "b10",
            title = "What does IS NOT NULL filter?",
            topic = "NULL Handling",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                SELECT order_id, shipped_date
                FROM orders
                WHERE shipped_date IS NOT NULL;
            """.trimIndent(),
            options = listOf(
                "Orders that have not been shipped yet",
                "Orders that have a confirmed, valid shipping date",
                "Orders where shipped_date is equal to the string 'NULL'",
                "Only cancelled orders"
            ),
            correctOptionIndex = 1,
            explanation = "IS NOT NULL filters for rows where the specified column actually contains a non-missing, non-null value, excluding records with missing shipment dates."
        ),
        Question(
            id = "b11",
            title = "Can calculated expressions be selected and aliased?",
            topic = "Calculated Columns",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                SELECT item_name, unit_price, quantity,
                       (unit_price * quantity) AS line_total
                FROM invoice_items;
            """.trimIndent(),
            options = listOf(
                "No, mathematical operations can only be done inside stored procedures",
                "Yes, arithmetic expressions are evaluated per row and assigned an alias",
                "Only addition and subtraction are allowed in SELECT",
                "line_total creates a new permanent column on disk"
            ),
            correctOptionIndex = 1,
            explanation = "SQL supports scalar arithmetic operators (+, -, *, /) directly in the SELECT list, creating dynamic calculated columns labelled with aliases using AS."
        ),
        Question(
            id = "b12",
            title = "What does the NOT operator do in this condition?",
            topic = "Logical Operators",
            difficulty = Difficulty.BASIC,
            sqlSnippet = """
                SELECT product_name, category
                FROM products
                WHERE NOT (category = 'Discontinued');
            """.trimIndent(),
            options = listOf(
                "Selects all products whose category is 'Discontinued'",
                "Selects products whose category is anything other than 'Discontinued'",
                "Permanently deletes discontinued categories",
                "Returns an empty result set"
            ),
            correctOptionIndex = 1,
            explanation = "The NOT operator negates any boolean expression that follows, returning rows where the inner condition is false."
        ),

        // ================= INTERMEDIATE =================
        Question(
            id = "i1",
            title = "What is the difference between WHERE and HAVING here?",
            topic = "GROUP BY & HAVING",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                SELECT department_id, COUNT(*) AS emp_count
                FROM employees
                WHERE status = 'Active'
                GROUP BY department_id
                HAVING COUNT(*) > 5;
            """.trimIndent(),
            options = listOf(
                "WHERE and HAVING are completely interchangeable in this query",
                "WHERE filters individual rows before grouping; HAVING filters aggregated groups",
                "HAVING filters individual rows before grouping; WHERE filters aggregated groups",
                "HAVING cannot use aggregate functions like COUNT(*)"
            ),
            correctOptionIndex = 1,
            explanation = "The WHERE clause filters individual rows before aggregation occurs (only active employees are counted). The HAVING clause evaluates conditions on the grouped, aggregated results (only departments with more than 5 active employees are returned)."
        ),
        Question(
            id = "i2",
            title = "What happens to customers with zero orders?",
            topic = "LEFT JOIN",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                SELECT c.customer_id, c.name, o.order_id
                FROM customers c
                LEFT JOIN orders o ON c.customer_id = o.customer_id;
            """.trimIndent(),
            options = listOf(
                "They are excluded from the output",
                "They appear in the output with o.order_id set to NULL",
                "The database throws an integrity exception",
                "They appear with order_id set to 0"
            ),
            correctOptionIndex = 1,
            explanation = "A LEFT JOIN preserves all rows from the left table ('customers'). If there is no matching row in the right table ('orders'), columns from 'orders' evaluate to NULL."
        ),
        Question(
            id = "i3",
            title = "How to find customers who have NEVER placed an order?",
            topic = "LEFT JOIN & NULL",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                SELECT c.customer_id, c.name
                FROM customers c
                LEFT JOIN orders o ON c.customer_id = o.customer_id
                WHERE o.order_id IS NULL;
            """.trimIndent(),
            options = listOf(
                "This query correctly identifies customers without orders",
                "This query returns customers whose orders have total 0",
                "This query will throw a syntax error",
                "An INNER JOIN must be used instead"
            ),
            correctOptionIndex = 0,
            explanation = "By using LEFT JOIN and filtering WHERE o.order_id IS NULL, we catch only those customers that had no matching record in the orders table. This is an idiomatic anti-join pattern."
        ),
        Question(
            id = "i4",
            title = "What value is produced when score is 85?",
            topic = "CASE WHEN Expressions",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                SELECT student_name,
                  CASE 
                    WHEN score >= 90 THEN 'A'
                    WHEN score >= 80 THEN 'B'
                    WHEN score >= 70 THEN 'C'
                    ELSE 'F'
                  END AS grade
                FROM exam_results;
            """.trimIndent(),
            options = listOf(
                "'A'",
                "'B'",
                "'C'",
                "'F'"
            ),
            correctOptionIndex = 1,
            explanation = "CASE expressions evaluate conditions sequentially from top to bottom. For 85, score >= 90 is false, but score >= 80 is true, immediately returning 'B' and stopping further evaluation."
        ),
        Question(
            id = "i5",
            title = "What does COALESCE return in this expression?",
            topic = "COALESCE Function",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                SELECT 
                  COALESCE(cell_phone, home_phone, office_phone, 'N/A') AS contact
                FROM contacts;
            """.trimIndent(),
            options = listOf(
                "A comma-separated concatenation of all four values",
                "The first non-NULL value among the arguments in left-to-right order",
                "Always 'N/A' unless all phones are identical",
                "The longest phone number"
            ),
            correctOptionIndex = 1,
            explanation = "COALESCE() evaluates its arguments in order and returns the very first non-NULL value. If cell_phone is NULL, it checks home_phone, then office_phone, and falls back to 'N/A' if all are NULL."
        ),
        Question(
            id = "i6",
            title = "What does an INNER JOIN return?",
            topic = "INNER JOIN",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                SELECT e.name, d.dept_name
                FROM employees e
                INNER JOIN departments d ON e.dept_id = d.id;
            """.trimIndent(),
            options = listOf(
                "All employees, even those with no assigned department",
                "All departments, even those with no employees",
                "Only rows where there is a matching dept_id in both tables",
                "A Cartesian cross product of all rows"
            ),
            correctOptionIndex = 2,
            explanation = "INNER JOIN returns exclusively the rows where the join predicate (e.dept_id = d.id) evaluates to TRUE in both tables. Any unmatched employee or empty department is omitted."
        ),
        Question(
            id = "i7",
            title = "What is the effect of GROUP BY on multiple columns?",
            topic = "Multi-column GROUP BY",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                SELECT region, product_category, SUM(sales_amount)
                FROM sales
                GROUP BY region, product_category;
            """.trimIndent(),
            options = listOf(
                "Sums sales per region, ignoring product category",
                "Sums sales per distinct pair of (region, product_category)",
                "Returns a separate table for each column",
                "Throws an error because GROUP BY only allows one column"
            ),
            correctOptionIndex = 1,
            explanation = "Grouping by multiple columns produces an aggregate bucket for every unique combination of values across those columns (e.g. North/Electronics, North/Clothing, South/Electronics)."
        ),
        Question(
            id = "i8",
            title = "Why must selected non-aggregated columns appear in GROUP BY?",
            topic = "GROUP BY Rules",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                -- In standard ANSI SQL mode:
                SELECT department_id, employee_name, AVG(salary)
                FROM employees
                GROUP BY department_id;
            """.trimIndent(),
            options = listOf(
                "It executes fine and picks the first employee_name arbitrarily",
                "It raises an error: employee_name is neither aggregated nor in GROUP BY",
                "employee_name is automatically converted into a sum",
                "AVG(salary) is calculated per employee instead of department"
            ),
            correctOptionIndex = 1,
            explanation = "In ANSI SQL, any column present in the SELECT list that is not wrapped in an aggregate function must be explicitly included in the GROUP BY clause. Otherwise, the database cannot determine which employee_name to display for a single department group."
        ),
        Question(
            id = "i9",
            title = "What does this Self-JOIN achieve?",
            topic = "Self JOIN",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                SELECT e.name AS employee, m.name AS manager
                FROM employees e
                LEFT JOIN employees m ON e.manager_id = m.id;
            """.trimIndent(),
            options = listOf(
                "Compares each employee's salary against all other employees",
                "Matches each employee with their respective manager in the same table",
                "Combines two completely separate tables with identical schemas",
                "Creates duplicate rows for every employee"
            ),
            correctOptionIndex = 1,
            explanation = "A Self-JOIN joins a table to itself using aliases (here 'e' for employee and 'm' for manager). It resolves hierarchical relationships within the same entity table, like employee reporting structures."
        ),
        Question(
            id = "i10",
            title = "What is the result of this CROSS JOIN?",
            topic = "CROSS JOIN",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                -- sizes table has 3 rows: S, M, L
                -- colors table has 4 rows: Red, Blue, Green, Black
                SELECT s.size_name, c.color_name
                FROM sizes s
                CROSS JOIN colors c;
            """.trimIndent(),
            options = listOf(
                "7 rows (3 + 4)",
                "12 rows (3 * 4 Cartesian product)",
                "3 rows",
                "Throws an error because there is no ON condition"
            ),
            correctOptionIndex = 1,
            explanation = "A CROSS JOIN produces the Cartesian product of two tables, pairing every row of the first table with every row of the second table (3 * 4 = 12 total combinations)."
        ),
        Question(
            id = "i11",
            title = "What does ROUND(val, 2) do?",
            topic = "Scalar Math Functions",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                SELECT ROUND(123.4567, 2) AS rounded_val;
            """.trimIndent(),
            options = listOf(
                "123.45",
                "123.46",
                "123.00",
                "120.00"
            ),
            correctOptionIndex = 1,
            explanation = "ROUND(number, decimal_places) rounds to the specified decimal position using standard round-half-up math. The digit after 5 is 6, so 123.4567 rounds up to 123.46."
        ),
        Question(
            id = "i12",
            title = "How does RIGHT JOIN differ from LEFT JOIN?",
            topic = "RIGHT JOIN",
            difficulty = Difficulty.INTERMEDIATE,
            sqlSnippet = """
                SELECT e.name, d.dept_name
                FROM employees e
                RIGHT JOIN departments d ON e.dept_id = d.id;
            """.trimIndent(),
            options = listOf(
                "All employees are preserved, departments without employees are dropped",
                "All departments are preserved, filling employee columns with NULL if vacant",
                "Only departments that have exactly one employee are shown",
                "It is identical to an INNER JOIN"
            ),
            correctOptionIndex = 1,
            explanation = "A RIGHT JOIN keeps all rows from the right table ('departments'), matching records from the left table where possible and inserting NULLs for vacant departments."
        ),

        // ================= ADVANCED =================
        Question(
            id = "a1",
            title = "What is the difference between ROW_NUMBER() and RANK()?",
            topic = "Window Functions",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                SELECT student_id, score,
                  ROW_NUMBER() OVER (ORDER BY score DESC) AS row_num,
                  RANK() OVER (ORDER BY score DESC) AS rnk
                FROM scores;
                -- Suppose scores are: 100, 95, 95, 90
            """.trimIndent(),
            options = listOf(
                "Both produce identical values: 1, 2, 3, 4",
                "ROW_NUMBER gives 1, 2, 3, 4; RANK gives 1, 2, 2, 4 (skips 3 after tie)",
                "ROW_NUMBER gives 1, 2, 2, 3; RANK gives 1, 2, 3, 4",
                "RANK gives 1, 2, 2, 3 without skipping any ranks"
            ),
            correctOptionIndex = 1,
            explanation = "ROW_NUMBER() assigns a unique sequential integer to each row regardless of ties (1, 2, 3, 4). RANK() assigns the same rank to identical values and skips subsequent ranks to reflect ties (1, 2, 2, 4). DENSE_RANK() would have assigned 1, 2, 2, 3."
        ),
        Question(
            id = "a2",
            title = "What does this cumulative window function compute?",
            topic = "Running Totals",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                SELECT txn_date, amount,
                  SUM(amount) OVER (
                    ORDER BY txn_date
                    ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
                  ) AS running_total
                FROM transactions;
            """.trimIndent(),
            options = listOf(
                "The grand total amount across all transactions on each row",
                "A running cumulative sum of amount from the start up to the current row",
                "The moving average of the last 3 days",
                "The difference in amount between consecutive days"
            ),
            correctOptionIndex = 1,
            explanation = "SUM() OVER (ORDER BY ... ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) computes a running cumulative total, aggregating all values from the first row up to the row currently being processed."
        ),
        Question(
            id = "a3",
            title = "What is the primary benefit of a Common Table Expression (CTE)?",
            topic = "CTEs (WITH Clause)",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                WITH RegionalSales AS (
                  SELECT region, SUM(amount) AS total_sales
                  FROM sales
                  GROUP BY region
                )
                SELECT region, total_sales
                FROM RegionalSales
                WHERE total_sales > 100000;
            """.trimIndent(),
            options = listOf(
                "It creates a permanent physical table on disk with indexes",
                "It creates a temporary, readable named result set scoped to the statement",
                "It bypasses table-level security rules and permissions",
                "It enables parallel distributed execution across clusters"
            ),
            correctOptionIndex = 1,
            explanation = "A Common Table Expression (CTE) defined via WITH acts as a temporary, named result set existing only during the execution of that specific statement. It greatly improves readability, modularity, and recursion compared to deeply nested subqueries."
        ),
        Question(
            id = "a4",
            title = "What does PARTITION BY do inside an OVER() clause?",
            topic = "Window Partitioning",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                SELECT emp_name, department_id, salary,
                  AVG(salary) OVER (
                    PARTITION BY department_id
                  ) AS dept_avg_salary
                FROM employees;
            """.trimIndent(),
            options = listOf(
                "Collapses all rows into a single row per department like GROUP BY",
                "Calculates department average while retaining individual employee rows",
                "Partitions the physical database table on disk into shards",
                "Deletes employees who make less than department average"
            ),
            correctOptionIndex = 1,
            explanation = "Unlike GROUP BY (which collapses rows into summary rows), PARTITION BY divides the window query result set into independent subsets without reducing the row count. Each employee row retains its identity while displaying their department's calculated average."
        ),
        Question(
            id = "a5",
            title = "What will LEAD(sales, 1) return for each row?",
            topic = "Lead & Lag Functions",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                SELECT month, sales,
                  LEAD(sales, 1) OVER (ORDER BY month) AS next_month_sales
                FROM monthly_revenue;
            """.trimIndent(),
            options = listOf(
                "The sales value from the preceding month",
                "The sales value from the subsequent (next) month",
                "The highest sales value of the year",
                "The difference between this month and the average"
            ),
            correctOptionIndex = 1,
            explanation = "LEAD(column, offset) accesses data from a subsequent row at a given physical offset without an expensive self-join. (LAG accesses previous rows). The last row will have NULL as next_month_sales."
        ),
        Question(
            id = "a6",
            title = "What is the critical difference between UNION and UNION ALL?",
            topic = "Set Operations",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                SELECT product_id FROM store_a_inventory
                UNION
                SELECT product_id FROM store_b_inventory;
            """.trimIndent(),
            options = listOf(
                "UNION combines sets and eliminates duplicates; UNION ALL keeps all duplicates",
                "UNION keeps all duplicates; UNION ALL eliminates duplicates",
                "UNION requires both tables to have identical primary keys",
                "UNION can only combine tables from different database schemas"
            ),
            correctOptionIndex = 0,
            explanation = "UNION performs a distinct operation on the combined results, sorting and removing duplicate rows (which adds performance overhead). UNION ALL simply concatenates the two datasets directly, preserving any duplicates and running faster."
        ),
        Question(
            id = "a7",
            title = "What does this correlated subquery evaluate?",
            topic = "Correlated Subqueries",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                SELECT e1.name, e1.salary, e1.department_id
                FROM employees e1
                WHERE e1.salary > (
                  SELECT AVG(e2.salary)
                  FROM employees e2
                  WHERE e2.department_id = e1.department_id
                );
            """.trimIndent(),
            options = listOf(
                "Employees who earn more than the company-wide average salary",
                "Employees who earn more than the average salary of their own department",
                "Only the department managers",
                "Employees who have the minimum salary in their department"
            ),
            correctOptionIndex = 1,
            explanation = "A correlated subquery references columns from the outer query (e2.department_id = e1.department_id). For every outer employee row, the subquery calculates the average salary of *that employee's specific department*, filtering for those above their department average."
        ),
        Question(
            id = "a8",
            title = "How does DENSE_RANK() handle ties compared to RANK()?",
            topic = "DENSE_RANK() vs RANK()",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                -- Values: 50, 40, 40, 30
                SELECT val,
                  DENSE_RANK() OVER (ORDER BY val DESC) AS d_rnk,
                  RANK() OVER (ORDER BY val DESC) AS rnk
                FROM items;
            """.trimIndent(),
            options = listOf(
                "DENSE_RANK produces: 1, 2, 2, 3; RANK produces: 1, 2, 2, 4",
                "DENSE_RANK produces: 1, 2, 3, 4; RANK produces: 1, 2, 2, 3",
                "Both produce 1, 2, 2, 4",
                "Both produce 1, 2, 3, 4"
            ),
            correctOptionIndex = 0,
            explanation = "DENSE_RANK() does not skip numbers after ties. Two tied rows receive rank 2, and the immediately following row receives rank 3 (dense sequence). Standard RANK() leaves a gap and assigns rank 4."
        ),
        Question(
            id = "a9",
            title = "What will this recursive CTE generate?",
            topic = "Recursive CTEs",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                WITH RECURSIVE NumberSeq AS (
                  SELECT 1 AS n
                  UNION ALL
                  SELECT n + 1 FROM NumberSeq WHERE n < 5
                )
                SELECT n FROM NumberSeq;
            """.trimIndent(),
            options = listOf(
                "An infinite loop that crashes the server",
                "The numbers 1, 2, 3, 4, 5",
                "The numbers 1 and 5 only",
                "A sequence from 5 down to 1"
            ),
            correctOptionIndex = 1,
            explanation = "A recursive CTE starts with an anchor member (n = 1), followed by a recursive step that adds 1 while n < 5. It terminates when the WHERE condition evaluates to false, yielding rows with n = 1, 2, 3, 4, 5."
        ),
        Question(
            id = "a10",
            title = "What is the advantage of EXISTS over IN with subqueries?",
            topic = "EXISTS vs IN",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                SELECT c.customer_id, c.name
                FROM customers c
                WHERE EXISTS (
                  SELECT 1 FROM orders o
                  WHERE o.customer_id = c.customer_id
                );
            """.trimIndent(),
            options = listOf(
                "EXISTS can short-circuit as soon as one match is found and handles NULLs safely",
                "IN is always faster than EXISTS in all databases",
                "EXISTS returns the sum of orders",
                "EXISTS creates an index on customer_id"
            ),
            correctOptionIndex = 0,
            explanation = "EXISTS short-circuits evaluation as soon as the first matching row is found in the subquery without loading entire value lists into memory, and avoids tricky NULL-handling pitfalls inherent to NOT IN."
        ),
        Question(
            id = "a11",
            title = "What does FIRST_VALUE() return in a window partition?",
            topic = "Window Functions (Value)",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                SELECT department_id, employee_name, salary,
                  FIRST_VALUE(employee_name) OVER (
                    PARTITION BY department_id
                    ORDER BY salary DESC
                  ) AS highest_earner
                FROM employees;
            """.trimIndent(),
            options = listOf(
                "The lowest earner in the entire company",
                "The name of the employee with the highest salary within that department",
                "The first employee hired regardless of salary",
                "A comma separated list of all earners"
            ),
            correctOptionIndex = 1,
            explanation = "FIRST_VALUE() returns the value of the specified column from the first row in the window frame. Partitioned by department and ordered by salary DESC, the first row is the highest earner in that department."
        ),
        Question(
            id = "a12",
            title = "Can multiple CTEs be chained in a single query?",
            topic = "Chained CTEs",
            difficulty = Difficulty.ADVANCED,
            sqlSnippet = """
                WITH 
                  DeptSummary AS (
                    SELECT dept_id, AVG(salary) AS avg_sal
                    FROM employees GROUP BY dept_id
                  ),
                  HighPayingDepts AS (
                    SELECT dept_id FROM DeptSummary WHERE avg_sal > 80000
                  )
                SELECT * FROM HighPayingDepts;
            """.trimIndent(),
            options = listOf(
                "No, only one WITH keyword and CTE is permitted per query",
                "Yes, multiple comma-separated CTEs can reference earlier CTEs in the chain",
                "Only recursive CTEs can be chained",
                "Chained CTEs require separate database transactions"
            ),
            correctOptionIndex = 1,
            explanation = "Standard SQL permits multiple CTE definitions separated by commas under a single WITH clause. Later CTEs can reference earlier CTEs in the same statement, enabling clean modular pipelines."
        )
    )

    fun getQuestionsByFilter(filter: DifficultyFilter): List<Question> {
        return when (filter) {
            DifficultyFilter.BASIC -> {
                questions.filter { it.difficulty == Difficulty.BASIC }.shuffled().take(10)
            }
            DifficultyFilter.INTERMEDIATE -> {
                questions.filter { it.difficulty == Difficulty.INTERMEDIATE }.shuffled().take(10)
            }
            DifficultyFilter.ADVANCED -> {
                questions.filter { it.difficulty == Difficulty.ADVANCED }.shuffled().take(10)
            }
            DifficultyFilter.MIXED -> {
                // Ensure a nice balanced mix across all difficulty levels:
                val basics = questions.filter { it.difficulty == Difficulty.BASIC }.shuffled().take(3)
                val intermediate = questions.filter { it.difficulty == Difficulty.INTERMEDIATE }.shuffled().take(4)
                val advanced = questions.filter { it.difficulty == Difficulty.ADVANCED }.shuffled().take(3)
                (basics + intermediate + advanced).shuffled()
            }
        }
    }

    fun getTotalQuestionCount(): Int = questions.size

    fun getCountByDifficulty(difficulty: Difficulty): Int =
        questions.count { it.difficulty == difficulty }
}
