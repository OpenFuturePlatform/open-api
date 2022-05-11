import {Button, Grid} from "semantic-ui-react";
import {Link} from "react-router-dom";
import TokenList from "../components/UserTokenList";
import React from "react";

const UserTokenDashboard = () => {
  return (
    <Grid>
      <Grid.Row>
        <Grid.Column width={16} floated="right">
          <Link to="/tokens/new">
            <Button
              primary
              style={{
                marginBottom: '10px',
                marginTop: '10px',
                backgroundColor: '#4c93e0'
              }}
              type="button"
              floated="right"
            >
              Add New Token
            </Button>
          </Link>
        </Grid.Column>
      </Grid.Row>
      <TokenList/>
    </Grid>
  );
};

export default UserTokenDashboard;
