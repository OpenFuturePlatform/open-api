import React, { Component } from 'react';
import { Button, Icon, Loader, Modal } from 'semantic-ui-react';
import { connect } from 'react-redux';
import * as actions from '../actions';

const ETHEREUM_NETWORK = process.env.REACT_APP_ETHEREUM_NETWORK;

class WithdrawModal extends Component {
  renderModalContent(withdrawModalInfo, closeWithdrawalModal) {
    if (withdrawModalInfo.events && withdrawModalInfo.events.incorrectDeveloperAddress) {
      return (
        <div>
          <div>{`Wrong developer address.  You must use address: ${
            withdrawModalInfo.events.incorrectDeveloperAddress.address
          }`}</div>
          <div>This is the developer address set for the scaffold.</div>
          <div>
            <a href={`https://${ETHEREUM_NETWORK}/tx/${this.props.withdrawModalInfo.transactionHash}`} target="_blank">
              Visit this link to see the transaction on-chain.
            </a>
          </div>
          <Modal.Actions>
            <Button
              basic
              color="red"
              inverted
              onClick={() => {
                this.props.closeWithdrawalModal();
              }}
            >
              <Icon name="close" /> Close
            </Button>
          </Modal.Actions>
        </div>
      );
    } else if (withdrawModalInfo.events && withdrawModalInfo.events.fundsDeposited) {
      return (
        <div>
          <a href={`https://${ETHEREUM_NETWORK}/tx/${this.props.withdrawModalInfo.transactionHash}`} target="_blank">
            Congratulations your funds have deposited to your developer address. Visit this link to see the transaction
            on-chain.
          </a>
          <Modal.Actions>
            <Button
              basic
              color="red"
              inverted
              onClick={() => {
                this.props.closeWithdrawalModal();
              }}
            >
              <Icon name="close" /> Close
            </Button>
          </Modal.Actions>
        </div>
      );
    } else {
      return <div>Open is withdrawing funds to your developer address. Please be patient this may take a while...</div>;
    }
  }

  render() {
    const { withdrawModalInfo, closeWithdrawalModal } = this.props;
    return (
      <Modal open={withdrawModalInfo.showModal} basic>
        <Modal.Content>
          <div style={{ height: '100px' }}>
            <Loader size="large" disabled={!withdrawModalInfo.showLoader} />
          </div>
          <div style={{ fontSize: '20px', textAlign: 'center' }}>
            {this.renderModalContent(withdrawModalInfo, closeWithdrawalModal)}
          </div>
        </Modal.Content>
      </Modal>
    );
  }
}

const mapStateToProps = state => {
  const { withdrawModalInfo } = state;
  return { withdrawModalInfo };
};

WithdrawModal = connect(
  mapStateToProps,
  actions
)(WithdrawModal);

export default WithdrawModal;
