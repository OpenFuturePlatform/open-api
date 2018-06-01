import React from 'react';
import SurveyList from './scaffolds/ScaffoldList';
import {Button, Grid} from 'semantic-ui-react';
import {Link} from "react-router-dom";

const Dashboard = () => {
  return (
    <Grid>
      <Grid.Row>
        <Grid.Column width={16} floated="right">
          <Link to="/scaffolds/new">
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
              Add Scaffold
            </Button>
          </Link>
        </Grid.Column>
      </Grid.Row>
      <SurveyList/>
    </Grid>
  );
};

export default Dashboard;
