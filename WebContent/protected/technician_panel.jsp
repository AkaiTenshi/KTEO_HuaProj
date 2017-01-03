<html>
  
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css"
    rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <title>Technician Panel</title>
  </head>
  
  <body>
  <div class="navbar navbar-default navbar-static-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-ex-collapse"></button>
          <a class="navbar-brand" href="#"><span>KTEO Intranet System</span></a>
        </div>
        <div class="collapse navbar-collapse" id="navbar-ex-collapse">
        <a class="btn btn-default navbar-btn" href="/KTEO/LogoutServlet">Logout</a>
          <ul class="nav navbar-nav navbar-right">
          <li class="active">
            	<p class="navbar-text">Logged in as: <%=request.getSession().getAttribute("User") %></p>
            </li> 
          </ul>
        </div>
      </div>
    </div>
  
    <div class="section">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <form class="form-inline" role="form" action="/KTEO/TechnicianPanelServlet" method="post">
              <div class="form-group" style="padding-left:5px">
                <label for="visual">Visual:</label>
                <textarea class="form-control" rows="5" name="visual">${visual}</textarea>
              </div>
              <div class="form-group" style="padding-left:25px">
                <label for="stability">Stability:</label>
                <textarea class="form-control" rows="5" name="stability">${stability}</textarea>
              </div>
              <div class="form-group">
                <label for="brakes">Brakes:</label>
                <textarea class="form-control" rows="5" name="brakes">${brakes}</textarea>
              </div>
              <div class="form-group">
                <label for="suspension">Suspension:</label>
                <textarea class="form-control" rows="5" name="suspension">${suspension}</textarea>
              </div>
              <div class="form-group" style="padding-left:20px">
                <label for="gas">Gas:</label>
                <textarea class="form-control" rows="5" name="gas">${gas}</textarea>
              </div>
              <div class="form-group" style="padding-left:38px">
                <label for="lights">Lights:</label>
                <textarea class="form-control" rows="5" name="lights">${lights}</textarea>
              </div>
              <br>
              <br>
               <div class="form-group">
                <div class="col-sm-2">
                  <label for="plates" class="control-label">License Plates</label>
                </div>
                <div class="col-sm-10">
                  <input type="search" class="form-control" placeholder="Enter License Plates"
                  name="plates" value="${plates}">
                </div>
                <div class="col-sm-5">
                  <select class="form-control" id="sel1" name="action">
                    <option value="check">Get data</option>
                    <option value="update">Update Database</option>
                  </select>
                  <button class="btn btn-default">Submit</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </body>

</html>