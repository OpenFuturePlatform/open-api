import {Button, Grid} from "semantic-ui-react";
import {Link} from "react-router-dom";
import SurveyList from "../components/GatewayApplicationList";
import React from "react";

const GatewayApplicationDashboard = () => {
    return (
        <Grid>
            <Grid.Row>
                <Grid.Column width={16} floated="right">
                    <Link to="/applications/new">
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
                            Add Application
                        </Button>
                    </Link>
                </Grid.Column>
            </Grid.Row>
            <SurveyList/>
        </Grid>
    );
};

export default GatewayApplicationDashboard;