import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
//import App from './App';


const inputParsers = {
  date(input) {
     const [month, day, year] =input.split('/');
    return `${year}-${month}-${day}`;
  },
  uppercase(input) {
    return input.toUpperCase();
  },
  number(input) {
    return parseFloat(input);
  },
};

class ShakingError extends React.Component {
	constructor() { 
  super(); 
  this.state = { key: 0 }; 
  }

	componentWillReceiveProps() {
    // update key to remount the component to rerun the animation
  	this.setState({ key: ++this.state.key });
  }
  
  render() {
  	return <div key={this.state.key} className="bounce">{this.props.text}</div>;
  }
}

class MyForm extends React.Component {
  constructor() {
    super();
    this.state = {};
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(event) {
    event.preventDefault();
    if (!event.target.checkValidity()) {
    	this.setState({
        invalid: true,
        displayErrors: true,
      });
      return;
    }
    const form = event.target;
    const data = new FormData(form);

    for (let name of data.keys()) {
      const input = form.elements[name];
      const parserName = input.dataset.parse;
      console.log('parser name is', parserName);
      if (parserName) {
        const parsedValue = inputParsers[parserName](data.get(name))
        data.set(name, parsedValue);
      }
    }
    
    this.setState({
    	res: stringifyFormData(data),
      invalid: false,
      displayErrors: false,
    });

 /*   fetch('/api/form-submit-url', {
      method: 'POST',
       body: data,
   });*/
  }

  render() {
  	const { res, invalid, displayErrors } = this.state;
    return (
    	<div>
        <form
          onSubmit={this.handleSubmit}
          noValidate
          className={displayErrors ? 'displayErrors' : ''}
         >
          <label htmlFor="location">Location:</label>
          <input
            id="location"
            name="location"
            type="text"
            data-parse="uppercase"
            required
          />

          <label htmlFor="datein">Date-in:</label>
          <input
            id="datein"
            name="datein"
            type="text"
            data-parse="date"
            placeholder="MM/DD//YYYY"
            pattern="\d{2}\/\d{2}/\d{4}"
            required
          />
          
           <label htmlFor="dateout">Date-out:</label>
          <input
            id="dateout"
            name="dateout"
            type="text"
            data-parse="date"
            placeholder="MM/DD//YYYY"
            pattern="\d{2}\/\d{2}/\d{4}"
            required
          />
          
         
          <label htmlFor="guests">Guests:</label>
          <input
            id="guests"
            name="guests"
            type="number" min="1"
            data-parse="number"
            placeholder="1"           
            required
          />
          
          
          <button>Search</button>
        </form>
        
        
        
        <div className="res-block">
          {invalid && (
            <ShakingError text="Form is not valid" />
          )}
          {!invalid && res && (
          	<div>
              <h3>Transformed data to be sent:</h3>
              <pre>FormData {res}</pre>
          	</div>
          )}
        </div>
    	</div>
    );
  }
}

ReactDOM.render(
	<MyForm />,
  document.getElementById("root")
);


function stringifyFormData(fd) {
  const data = {};
	for (let key of fd.keys()) {
  	data[key] = fd.get(key);
  }
  return JSON.stringify(data, null, 2);
}

//ReactDOM.render(<App />, document.getElementById('root'));

