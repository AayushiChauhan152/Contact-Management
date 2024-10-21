//theme code
let curTheme = getTheme();

changeTheme()

function changeTheme() {

	document.querySelector('html').classList.add(curTheme);
	const changeThemebtn = document.querySelector('#theme_btn')
	changeThemebtn.addEventListener("click", () => {

		document.querySelector('html').classList.remove(curTheme);

		if (curTheme == "dark") {
			curTheme = "light"

		} else {
			curTheme = "dark"
		}
		setTheme(curTheme)

		document.querySelector('html').classList.add(curTheme);

		changeThemebtn.querySelector('span').textContent = curTheme;
	})
}

function setTheme(theme) {
	localStorage.setItem("theme", theme);
}

function getTheme() {
	let theme = localStorage.getItem("theme");
	if (theme) return theme;
	else return "light";
}


console.log("admin")


//profile image code

document.querySelector("#img_field").addEventListener('change', function(e) {
	let file = e.target.files[0];
	let reader = new FileReader();

	reader.onload = function() {
		document.getElementById("img_preview").src = reader.result;
	};
	reader.readAsDataURL(file);
})

// search contacts
function search() {
	let keyword = document.getElementById("search-input").value;
	// Redirect to the first page of search results
	window.location.href = "/user/contacts/0?keyword=" + encodeURIComponent(keyword);
}


// contact modal
const $targetEl = document.getElementById('default-modal');

// options with default values
const options = {
	placement: 'bottom-right',
	backdrop: 'dynamic',
	backdropClasses:
		'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
	closable: true,
	onHide: () => {
		console.log('modal is hidden');
	},
	onShow: () => {
		console.log('modal is shown');
	},
	onToggle: () => {
		console.log('modal has been toggled');
	},
};

// instance options object
const instanceOptions = {
	id: 'default-modal',
	override: true
};

const modal = new Modal($targetEl, options, instanceOptions);
function openModal() {
	modal.show();
}

function loadContact(id) {
	console.log(id);

	// Fetch the contact details from the API
	fetch(`http://localhost:8080/api/contacts/${id}`)
		.then((response) => response.json())  // Convert the response to JSON
		.then((data) => {
			console.log(data);  // Log the fetched data

			// Update the modal with the fetched data
			document.querySelector("#contact_name").innerHTML = data.cname;
			document.querySelector("#contact_email").innerHTML = data.email;
			document.querySelector("#contact_phone").innerHTML = data.phone;
			document.querySelector("#contact_address").innerHTML = data.address;
			document.querySelector("#contact_work").innerHTML = data.work;
			document.querySelector("#contact_detail").innerHTML = data.detail;

			// Update the image source or set a default if it's missing
			const profileImgElement = document.querySelector("#contact_image");
			if (data.profileImg) {
				profileImgElement.src = data.profileImg;
			} else {
				profileImgElement.src = 'default_image_path';  // Handle missing image case
			}

			// Show the modal (assuming you're using a method to display the modal)
			document.querySelector("#default-modal").classList.remove("hidden");
		})
		.catch((error) => {
			console.log(error);  // Handle errors
		});
}

function exportData() {
	console.log("hihihihi")
	
	TableToExcel.convert(document.getElementById("contact-table"), {
		name: "contact-list.xlsx",
		sheet: {
			name: "Sheet 1",
		},
	});
}
