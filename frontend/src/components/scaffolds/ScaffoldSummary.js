import React, {Component} from 'react';
import {Button, Card, Grid} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {convertCurrencies} from '../../actions';
import {deactivateScaffold, fetchScaffoldItem} from "../../actions/index";

class ScaffoldSummary extends Component {

  componentDidMount() {
    const scaffoldAddress = this.props.match.params.scaffoldAddress;
    this.props.actions.fetchScaffoldItem(scaffoldAddress);
  }

  renderStatus() {
    const {onchainScaffoldSummary} = this.props;
    const scaffoldAddress = this.props.match.params.scaffoldAddress;
    const status = onchainScaffoldSummary.enabled;
    const activateMessage = (
      <div style={{color: 'red'}}>
        Your scaffold is created but is inactive. To activate your scaffold you need to transfer 10 OPEN Tokens to it.
      </div>
    );
    const deactivateButton = <Button onClick={() => this.props.actions.deactivateScaffold(scaffoldAddress)}>Deactivate</Button>

    return (
      <div>
        Status: {status ? 'Active' : 'Disabled'}
        <div style={{marginTop: '10px'}}>
          {status ? deactivateButton : activateMessage}
        </div>
      </div>
    )
  }

  render() {
    const {onchainScaffoldSummary} = this.props;

    return (
      <div style={{marginTop: '20px'}}>
        <Grid>
          <Grid.Row>
            <Grid.Column width={16}>
              <Card fluid>
                <Card.Content header="On-chain Scaffold Summary" meta="This data is coming from the Ethereum Network"/>
                <Card.Content>
                  {this.renderStatus()}
                </Card.Content>
                <Card.Content>
                  <div>Scaffold Description: {onchainScaffoldSummary.description}</div>
                  <div>Scaffold Owner Address: {onchainScaffoldSummary.vendorAddress}</div>
                </Card.Content>
                <Card.Content>
                  <div>
                    <div style={{width: '64%', display: 'inline-block'}}>
                      Scaffold Amount: {(Number.parseFloat(onchainScaffoldSummary.amount)).toFixed(5)} ether
                    </div>
                    <div style={{width: '34%', display: 'inline-block'}}>
                      {onchainScaffoldSummary.fiatAmount} {onchainScaffoldSummary.fiatCurrency}
                    </div>
                  </div>
                  <div>Scaffold Transactions: {onchainScaffoldSummary.transactionIndex}</div>
                </Card.Content>
              </Card>
            </Grid.Column>
          </Grid.Row>
          <Grid.Row>
            <Grid.Column/>
          </Grid.Row>
        </Grid>
      </div>
    );
  }
}

const mapStateToProps = ({auth, onchainScaffoldSummary, currencyConversionValue}) =>
  ({auth, onchainScaffoldSummary, currencyConversionValue});

const mapDispatchToProps = dispatch =>
  ({actions: bindActionCreators({convertCurrencies, fetchScaffoldItem, deactivateScaffold}, dispatch)});

ScaffoldSummary = connect(mapStateToProps, mapDispatchToProps)(ScaffoldSummary);

export default ScaffoldSummary;
