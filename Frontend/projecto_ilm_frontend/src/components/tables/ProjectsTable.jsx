import "./Tables.css";

export default function ProjectsTable() {
   return (
      <>
         <table className="centered-table">
            <thead>
               <tr>
                  <th>Project</th>
                  <th>Lab</th>
                  <th>Status</th>
                  <th>Start date | Final date</th>
                  <th>Members</th>
               </tr>
            </thead>
            <tbody>
               <tr>
                  <td>Row 1 Data 1</td>
                  <td>Row 1 Data 2</td>
                  <td>Row 1 Data 3</td>
                  <td>Row 1 Data 4</td>
                  <td>Row 1 Data 4</td>
               </tr>
               <tr>
                  <td>Row 2 Data 1</td>
                  <td>Row 2 Data 2</td>
                  <td>Row 2 Data 3</td>
                  <td>Row 1 Data 4</td>
                  <td>Row 1 Data 4</td>
               </tr>
               <tr>
                  <td>Row 2 Data 1</td>
                  <td>Row 2 Data 2</td>
                  <td>Row 2 Data 3</td>
                  <td>Row 2 Data 4</td>
                  <td>Row 1 Data 4</td>
               </tr>
               <tr>
                  <td>Row 2 Data 1</td>
                  <td>Row 2 Data 2</td>
                  <td>Row 2 Data 3</td>
                  <td>Row 2 Data 4</td>
                  <td>Row 1 Data 4</td>
               </tr>
            </tbody>
         </table>
      </>
   );
}
