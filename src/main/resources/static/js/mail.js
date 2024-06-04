async function mailConfirm(email) {

    const result = await axios.post(`/api/mail/email-validate` ,  {params: email})

    return result;
}


async function mailCert(bno) {


    // console.log(result)
    // return result.data
    return result;
}

