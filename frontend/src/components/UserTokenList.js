import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Card, Divider, Grid, Icon} from 'semantic-ui-react';
import {ProjectPagination} from '../components-ui/ProjectPagination';
import {GATEWAY_LIMIT, TOKEN_LIMIT} from '../const';
import {WordWrap} from "../components-ui/WordWrap";
import {Link} from "react-router-dom";
import {fetchUserTokens} from "../actions/user-token";


class UserTokenList extends Component {
  componentDidMount() {
    this.fetchUserTokens();
  }

  fetchUserTokens = (offset = 0, limit = GATEWAY_LIMIT) => {
    this.props.fetchUserTokens(offset, limit);
  };

  renderApplications() {
    const tokens = this.props.tokens;
    console.log(tokens);
    return tokens.list.map((token, index) => {
      return (
        <Card fluid key={index}>
          <Card.Content>

            <Grid.Row>
              <Grid.Column width={8}>
                <Link to={`/tokens/${token.id}`}>
                  <Card.Header>
                    <WordWrap>{token.name}</WordWrap>
                  </Card.Header>
                </Link>
                <div className="address">
                  Address: {token.address}
                </div>
              </Grid.Column>
            </Grid.Row>
          </Card.Content>

        </Card>
      );
    });
  }

  render() {
    const tokens = this.props.tokens;
    return (
      <Grid.Row>
        <Grid.Column width={16}>
          {this.renderApplications()}
          <ProjectPagination limit={TOKEN_LIMIT} totalCount={tokens.totalCount} onChange={this.fetchUserTokens} />
        </Grid.Column>
      </Grid.Row>
    );
  }
}
const mapStateToProps = ({ ercTokens }) => ({ tokens: ercTokens });

export default connect(
  mapStateToProps,
  {
    fetchUserTokens
  }
)(UserTokenList);
