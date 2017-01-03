<html>
  
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css"
    rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <title>Secretary Panel</title>
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
          <div class="col-md-6" style="padding:20px">
            <form role="form" action="/KTEO/SecretaryPanelServlet" method="post">
              <div class="form-group">
                <label class="control-label" for="exampleInputEmail1">License Plates</label>
                <input class="form-control" id="exampleInputEmail1" value="${plates}"
                placeholder="Enter license plates number" name="plates" type="search">
              </div>
              <div class="form-group">
                <label class="control-label" for="exampleInputPassword1">Owner</label>
                <input class="form-control" id="exampleInputPassword1" value="${owner}" 
                type="text" name="owner">
              </div>
              <div class="form-group">
                <label class="control-label" for="exampleInputPassword1">Model</label>
                <input class="form-control" id="exampleInputPassword1" value="${model}" 
                type="text" name="model">
              </div>
              <div class="form-group">
                <label class="control-label" for="exampleInputPassword1">Type</label>
                <input class="form-control" id="exampleInputPassword1" value="${type}" 
                type="text" name="type">
              </div>
              <div class="form-group">
                <label class="control-label" for="exampleInputPassword1">Cubics</label>
                <input class="form-control" id="exampleInputPassword1" value="${cubics}" 
                type="text" name="cubics">
              </div>
              <div class="form-group">
                <label class="control-label" for="exampleInputPassword1">Last Check Date</label>
                <input class="form-control" id="exampleInputPassword1" value="${date}"
                type="text" name="date">
              </div>
              <div class="form-group">
  				<label for="sel1">Select list:</label>
  				<select class="form-control" id="sel1" name="action">
				    <option value="check">Get data</option>
				    <option value="update">Update Database</option>
			  	</select>
			  </div>
            <button class="btn btn-default" type="submit">Submit</button>
            </form>
          </div>
          <div class="col-md-6" style="padding:20px">
            <div class="form-group">
              <label class="control-label" for="exampleInputEmail1">Non Insurance Fine</label>
              <input class="form-control" name="i_fine" id="exampleInputEmail1" value="${i_fine}"
              type="text">
            </div>
            <div class="form-group">
              <label class="control-label" for="exampleInputPassword1">Overdue Fine</label>
              <input class="form-control" name="o_fine" id="exampleInputPassword1"
              type="text">
            </div>
            
          </div>
        </div>
      </div>
    </div>
  </body>

</html>