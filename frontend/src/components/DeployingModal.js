import React, { Component } from 'react';
import { Button, Icon, Loader, Modal } from 'semantic-ui-react';
import { connect } from 'react-redux';
import * as actions from '../actions';

class WaitingModal extends Component {
  renderCloseButton = () => {
    const { closeModal } = this.props;
    return (
      <Modal.Actions>
        <Button basic color="red" inverted onClick={closeModal}>
          <Icon name="close" /> Close
        </Button>
      </Modal.Actions>
    );
  };

  renderMessage = () => {
    const { modalInfo } = this.props;

    if (modalInfo.error) {
      return (
        <div>
          <div style={{ color: 'red' }}>{modalInfo.error}</div>
          {this.renderCloseButton()}
        </div>
      );
    }

    if (modalInfo.contract) {
      return (
        <div>
          <div>Congratulations your scaffold is deployed.</div>;
          {this.renderCloseButton()}
        </div>
      );
    }

    return <div>Open is deploying your scaffold on-chain. Please be patient this may take a while...</div>;
  };

  render() {
    const { modalInfo } = this.props;
    return (
      <Modal open={modalInfo.showModal} basic>
        <Modal.Content>
          <div style={{ height: '100px' }}>
            <Loader size="large" disabled={!modalInfo.showLoader} />
          </div>
          <div style={{ fontSize: '20px', textAlign: 'center' }}>{this.renderMessage()}</div>
        </Modal.Content>
      </Modal>
    );
  }
}

const mapStateToProps = state => {
  const { modalInfo } = state;
  return { modalInfo };
};

WaitingModal = connect(
  mapStateToProps,
  actions
)(WaitingModal);

export default WaitingModal;
