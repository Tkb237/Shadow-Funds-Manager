package util.template;

import java.text.DecimalFormat;

public class Template
{
    public static DecimalFormat df = new DecimalFormat("#.00");
    public static String template = """
            <!DOCTYPE html>

            <html lang="de">

            <head>

                <meta charset="utf-8">

                <title>$user_name's report</title>
     
                <style>
                        #pTipp::first-letter
                        {
                            text-transform: uppercase;
                            font-size: xxx-large;
                        }
                    </style>

            </head>

            <body style="font-family: Bahnschrift,serif;">

            <h1 style="text-align: center; font-style: italic"><span style="text-transform: capitalize;">$user_name</span>'s report of <span style="text-transform: capitalize;">$date</span>
            </h1>


            <div id="ausgabe">
                <table style="border-collapse: collapse; width: 100%; border-radius: 10px; overflow: hidden;">

                    <tr style="background-color: #f2f2f2;">
                        <th colspan="3"  style="padding: 8px; border: 1px solid #ddd; font-size: x-large">Ausgabe</th>
                    </tr>
                    <tr style="background-color: #f2f2f2;">

                        <th style="padding: 8px; border: 1px solid #ddd;">Transaktion</th>

                        <th style="padding: 8px; border: 1px solid #ddd;">Datum</th>

                        <th style="padding: 8px; border: 1px solid #ddd;">Betrag</th>

                    </tr>

                    <tbody>

                    $ausgabe_data

                    </tbody>

                    <tfoot>

                    <tr style="background-color: #f9f9f9;">

                        <td colspan="2" style="padding: 8px; border: 1px solid #ddd; font-weight: bolder">Summe</td>

                        <td style="padding: 8px; border: 1px solid #ddd; text-align: center">$ausgabe_summe</td>

                    </tr>

                    </tfoot>

                </table>

            </div>

            <br>
            <br>

            <div id="eingabe">


                <table style="border-collapse: collapse; width: 100%; border-radius: 5px; overflow: hidden;">

                    <tr style="background-color: #f2f2f2;">
                        <th colspan="3"  style="padding: 8px; border: 1px solid #ddd; font-size: x-large">Eingabe</th>
                    </tr>

                    <tr style="background-color: #f2f2f2;">

                        <th style="padding: 8px; border: 1px solid #ddd;">Transaktion</th>

                        <th style="padding: 8px; border: 1px solid #ddd;">Datum</th>

                        <th style="padding: 8px; border: 1px solid #ddd;">Betrag</th>

                    </tr>

                    <tbody>

                    $eingabe_data

                    </tbody>

                    <tfoot>

                    <tr style="background-color: #f9f9f9;">

                        <td colspan="2" style="padding: 8px; border: 1px solid #ddd;font-weight: bolder">Summe</td>

                        <td style="padding: 8px; border: 1px solid #ddd; text-align: center">$eingabe_summe</td>

                    </tr>

                    </tfoot>

                </table>

            </div>
            <br>
            <br>
            <div id="darlehen">


                <table style="border-collapse: collapse; width: 100%; border-radius: 10px; overflow: hidden;">

                    <thead>
                    <tr style="background-color: #f2f2f2;">
                        <th colspan="3"  style="padding: 8px; border: 1px solid #ddd; font-size: x-large">Darlehen</th>
                    </tr>

                    <tr style="background-color: #f2f2f2;">

                        <th style="padding: 8px; border: 1px solid #ddd;">Transaktion</th>

                        <th style="padding: 8px; border: 1px solid #ddd;">Datum</th>

                        <th style="padding: 8px; border: 1px solid #ddd;">Betrag</th>

                    </tr>

                    </thead>

                    <tbody>

                    $darlehen_data

                    </tbody>

                    <tfoot>

                    <tr style="background-color: #f9f9f9;">

                        <td colspan="2" style="padding: 8px; border: 1px solid #ddd;font-weight: bolder">Summe</td>

                        <td style="padding: 8px; border: 1px solid #ddd; text-align: center">$darlehen_summe</td>

                    </tr>

                    </tfoot>

                </table>

            </div>
            <br>
            <br>
            <div id="schuld">

                <table style="border-collapse: collapse; width: 100%; border-radius: 10px; overflow: hidden;">

                    <tr style="background-color: #f2f2f2;">
                        <th colspan="3"  style="padding: 8px; border: 1px solid #ddd; font-size: x-large">Schuld</th>
                    </tr>

                    <tr style="background-color: #f2f2f2;">

                        <th style="padding: 8px; border: 1px solid #ddd;">Transaktion</th>

                        <th style="padding: 8px; border: 1px solid #ddd;">Datum</th>

                        <th style="padding: 8px; border: 1px solid #ddd;">Betrag</th>

                    </tr>

                    <tbody>

                    $schuld_data

                    </tbody>

                    <tfoot>

                    <tr style="background-color: #f9f9f9;">

                        <td colspan="2" style="padding: 8px; border: 1px solid #ddd;font-weight: bolder">Summe</td>

                        <td style="padding: 8px; border: 1px solid #ddd;; text-align: center">$schuld_summe</td>

                    </tr>

                    </tfoot>

                </table>

            </div>
            </body>

            </html>""";

    public static  String help = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>ShadowFunds Manager Help Menu</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 20px;
                        line-height: 1.6;
                    }
                    h1, h2 {
                        color: #2c3e50;
                    }
                    ul {
                        list-style-type: disc;
                        margin-left: 20px;
                    }
                    a {
                        color: #2980b9;
                        text-decoration: none;
                    }
                    a:hover {
                        text-decoration: underline;
                    }
                </style>
            </head>
            <body>
                <h1>Help Menu for ShadowFunds Manager (SFM)</h1>
                        
                <h2>1. Introduction</h2>
                <p><strong>Welcome to ShadowFunds Manager (SFM)!</strong></p>
                <p>SFM is a program dedicated to managing funds and monitoring your cash inflows and outflows, complemented by dynamic graphs for better visualization.</p>
                        
                <h2>2. Key Features</h2>
                <ul>
                    <li><strong>Task Management</strong>
                        <ul>
                            <li>Add tasks using the <code>+</code> and <code>-</code> keys when the <strong>Tree View</strong> is focused.</li>
                            <li>To add a task directly to the root, use the <strong>Add</strong> button in the <strong>Tree View</strong>.</li>
                            <li>To add a sub-task, select an existing task, then press <code>+</code> or right-click and choose <strong>Add</strong>.</li>
                            <li>Tasks are color-coded based on their priority.</li>
                        </ul>
                    </li>
                    <li><strong>Reports</strong>
                        <ul>
                            <li>The <strong>Report</strong> button generates a report for the current month in <strong>.html</strong> format. Reports are saved in the <strong>report</strong> folder located in the same directory as the executable.</li>
                        </ul>
                    </li>
                    <li><strong>Dynamic Charts</strong>
                        <ul>
                            <li>Access the charts by navigating to <strong>More</strong> -> <strong>Charts</strong>.</li>
                            <li>The <strong>PC Period</strong> chart allows you to visualize data for different time periods. A <strong>Generate Report</strong> button is available to generate a report for the desired month or year.</li>
                            <li>Double-click on the charts for an enlarged view.</li>
                        </ul>
                    </li>
                </ul>
                        
                <h2>3. Troubleshooting</h2>
                <ul>
                    <li><strong>Backup Issues</strong>
                        <ul>
                            <li>If you encounter difficulties, a default backup is available in the <strong>Conf</strong> folder that you can use to test the program.</li>
                        </ul>
                    </li>
                    <li><strong>Data Loading</strong>
                        <ul>
                            <li>Upon starting the application, the last saved data will be automatically loaded.</li>
                        </ul>
                    </li>
                </ul>
                        
                <h2>4. Support</h2>
                <ul>
                    <li><strong>Contact Us</strong>
                        <ul>
                            <li>If you need further assistance, feel free to leave a comment.</li>
                        </ul>
                    </li>
                </ul>
                        
                <h2>5. Useful Links</h2>
                <ul>
                        <li><strong>JavaFX:</strong> <a href="https://openjfx.io/" target="_blank">openjfx</a></li>
                </ul>
            </body>
            </html>""";
}
