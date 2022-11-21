import React from "react";
import {Segment} from "semantic-ui-react";
import {connect} from "react-redux";


import {Table} from "../components-ui/Table";
import {WalletGenerate} from "./GatewayApplicationWalletGenerate";
import {
  exportApplicationWalletPrivateKey,
  generateGatewayApplicationWallet, importGatewayApplicationWallet,
  removeGatewayApplicationWallet
} from "../actions/gateway-wallet";

import {getGatewayWalletSelector} from "../selectors/getGatewayWalletsSelector";

import {GatewayApplicationWalletRemove} from "./GatewayApplicationWalletRemove";
import {WalletImport} from "./GatewayApplicationWalletImport";
import {OpenScanLink} from "../components-ui/OpenScanLink";

const getColumns = (wallets, onRemove, onExport) => [
    {
      Header: 'Wallet Type',
      accessor: 'walletType',
      width: 200,
      Cell: ({ value }) => <span>{value}</span>,
      sortable: false
    },
    {
        Header: 'Wallet Address',
        accessor: 'address',
        Cell: ({ value }) => <OpenScanLink>{value}</OpenScanLink>,
        sortable: false
    },
    {
        Header: 'Blockchain',
        accessor: 'blockchain',
        width: 200,
        Cell: ({ value }) => <span>{value}</span>,
        sortable: false
    },
    {
        Header: '',
        accessor: 'address',
        width: 150,
        Cell: ({ value }) => (
            <span>
                <GatewayApplicationWalletRemove onSubmit={() => onRemove(value)} />
             </span>
        ),
        sortable: false
    },
    /*{
      Header: '',
      accessor: 'address',
      width: 150,
      Cell: (props) => (
        <span>
            <GatewayApplicationWalletPrivateKey onSubmit={() => onExport(props.row._original.address,props.row._original.blockchain)}/>
        </span>
      ),
      sortable: false
    }*/
];

class GatewayApplicationWalletComponent extends React.Component {

    onRemoveWallet = address => {
        const { gateway } = this.props;
        return this.props.removeGatewayApplicationWallet(gateway.id, address);
    };

    onExportPrivateKey = (address, blockchain) => {
      return this.props.exportApplicationWalletPrivateKey(address, blockchain);
    }

    onGenerateWallet = blockchain => {
        const { gateway } = this.props;
        this.props.generateGatewayApplicationWallet({ applicationId: gateway.id, webHook: gateway.webHook}, blockchain);
    }

    onImportWallet = walletImport => {
      this.props.importGatewayApplicationWallet(walletImport.walletRequest, {address: walletImport.address, blockchain: walletImport.blockchain});
    }

    render() {
        const { wallets, gateway } = this.props;

        const columns = getColumns(wallets.list, this.onRemoveWallet, this.onExportPrivateKey);
        const noDataText = 'No Wallet exist'
        return (
            <div className="table-with-add">
                <WalletGenerate  gateway={gateway} onSubmit={this.onGenerateWallet} />

                <Segment attached styles={{ padding: 0 }}>
                    <Table data={wallets.list} columns={columns} noDataText={noDataText} />
                </Segment>
            </div>
        );
    }
}

const mapStateToProps = (state, { gateway}) => {
    const wallets = getGatewayWalletSelector(state, gateway.id);
    return { gateway, wallets };
};

export const GatewayApplicationWallet = connect(
    mapStateToProps,
    {
        generateGatewayApplicationWallet,
        importGatewayApplicationWallet,
        removeGatewayApplicationWallet,
        exportApplicationWalletPrivateKey
    }
)(GatewayApplicationWalletComponent);
